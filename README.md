# Background Remover


| Before                                                 | After
| ----------------------------------------------------- | ---------------------------------------------------- |
| <img src="https://firebasestorage.googleapis.com/v0/b/test-bdb24.appspot.com/o/before.png?alt=media&token=1f1787f4-1d3d-4b25-8f3e-3fd50b1db408" width="200"/> | <img src="https://firebasestorage.googleapis.com/v0/b/test-bdb24.appspot.com/o/after.png?alt=media&token=5c65be26-9f14-48fb-bb41-6e7a41de613d" width="200"/>

## Overview
The `background_remover` package is the first Flutter plugin designed for easy integration of background removal functionality into Flutter applications. This package provides a straightforward way to pick images from the gallery and process them to remove their background.

## Features
- Image picking from gallery
- Background removal from images
- Display of images with and without background

## Getting Started
To use the `background_remover` package, follow these steps:

### Installation
Add `background_remover` to your `pubspec.yaml` file:

```yaml
dependencies:
  flutter:
    sdk: flutter
  background_remover: ^1.0.0
```

### Import the Package
Import the package along with other required packages in your Dart file:

```dart
import 'package:background_remover/background_remover.dart';
```

### Usage
Here is a basic example of how to use the `background_remover` package in a Flutter application:

```dart
Uint8List imageBytes = your image as byte;

removeBackground(imageBytes: imageBytes).then((Uint8List backgroundRemoveBytes) {
// then do whatever you want with byte of images such as display it in image.memory()..
});
```

`removeBackground()` is the method that handles the background removal process.

## Contributing
Contributions to the `background_remover` package are welcome. Please feel free to fork the repository, make changes, and create a pull request to upgrade it.

## License
This package is licensed under the [Apache License](LICENSE).

---

This README provides a basic introduction to the `background_remover` package. It assumes the reader has a fundamental understanding of Flutter and Dart.