import 'dart:async';
import 'package:flutter/services.dart';

const MethodChannel _channel = MethodChannel('background_remover');

class BackgroundRemover {
  static Future<dynamic> removeBackground({required Uint8List imageBytes}) async {
    return await _channel.invokeMethod(
        'removeBackground', <String, Uint8List>{'imageBytes': imageBytes});
  }
}
