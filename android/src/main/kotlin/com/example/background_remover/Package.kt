package com.example.background_remover

import android.annotation.TargetApi
import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import java.util.*
import android.graphics.Bitmap
import com.example.background_remover.BackgroundRemover
import com.example.background_remover.OnBackgroundChangeListener
import android.graphics.Color
import androidx.annotation.NonNull
import android.graphics.drawable.BitmapDrawable
import androidx.core.graphics.drawable.toBitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream
import android.graphics.Canvas
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.Registrar


class Package: FlutterPlugin, MethodCallHandler, ActivityAware {
  private lateinit var mChannel: MethodChannel
  private var activity: Activity? = null

  override fun onAttachedToActivity(binding: ActivityPluginBinding) {
    activity = binding.activity
  }

  override fun onDetachedFromActivity() {
    activity = null
  }

  override fun onDetachedFromActivityForConfigChanges() {
    activity = null
  }

  override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
    activity = binding.activity
  }

  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    setupCallbackChannels(flutterPluginBinding.binaryMessenger)
  }

  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    teardown()
  }

  private fun setupCallbackChannels(messenger: BinaryMessenger) {
    mChannel = MethodChannel(messenger, "background_remover")
    mChannel.setMethodCallHandler(this)
  }

  private fun teardown() {
    mChannel.setMethodCallHandler(null)
  }

  companion object {
    @JvmStatic
    fun registerWith(registrar: Registrar) {
      val inst = Package()
      inst.activity = registrar.activity()
      inst.setupCallbackChannels(registrar.messenger())
    }
  }

  override fun onMethodCall(call: MethodCall, result: Result) {
    when (call.method) {
        "removeBackground" -> {
          val imageBytes = call.argument<ByteArray>("imageBytes")
          if (imageBytes != null) {
              removeBackground(imageBytes, result)
          } else {
              result.error("INVALID_ARGUMENT", "Image bytes are null", null)
          }
        }
        else -> result.notImplemented()
    }
  }

  fun Bitmap.toByteArray(): ByteArray {
      val stream = ByteArrayOutputStream()
      this.compress(Bitmap.CompressFormat.PNG, 100, stream)
      return stream.toByteArray()
  }

  private fun removeBackground(imageBytes: ByteArray, result: MethodChannel.Result) {
      try {
          val resources = activity?.resources ?: return
          val drawable = BitmapDrawable(resources, BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size))
          BackgroundRemover.bitmapForProcessing(drawable.toBitmap(), true, object : OnBackgroundChangeListener {
              override fun onSuccess(bitmap: Bitmap) {
                  val originalWidth = bitmap.width
                  val originalHeight = bitmap.height
                  val targetWidth = 256
                  val targetHeight = (originalHeight.toFloat() / originalWidth.toFloat() * targetWidth).toInt()
                  val resizedBitmap = Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, true)
                  val processedBitmapWithWhiteBackground = Bitmap.createBitmap(targetWidth, targetHeight, Bitmap.Config.ARGB_8888)
                  val canvas = Canvas(processedBitmapWithWhiteBackground)

                  canvas.drawColor(Color.WHITE)
                  val left = (targetWidth - resizedBitmap.width) / 2f
                  val top = (targetHeight - resizedBitmap.height) / 2f

                  canvas.drawBitmap(resizedBitmap, left, top, null)
                  val outputBytes = ByteArrayOutputStream()

                  processedBitmapWithWhiteBackground.compress(Bitmap.CompressFormat.PNG, 100, outputBytes)
                  val processedImageBytes = outputBytes.toByteArray()
                  result.success(processedImageBytes)
              }
              override fun onFailed(exception: Exception) {
                  result.error("PROCESSING_ERROR", "Error processing image", null)
              }
          })
      } catch (e: Exception) {
          result.error("PROCESSING_ERROR", "Error processing image", null)
      }
  }
}
