import 'dart:async';
import 'dart:typed_data';

import 'src/background_remover_platform.dart';

Future<dynamic> removeBackground({required Uint8List imageBytes}) async {
  return await BackgroundRemover.removeBackground(
    imageBytes: imageBytes,
  );
}
