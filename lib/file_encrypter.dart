import 'dart:async';

import 'package:flutter/services.dart';
import 'package:meta/meta.dart';

/// File Encrypter
class FileEncrypter {
  static const _channel = const MethodChannel('file_encrypter');

  /// Encrypts file in [inFileName] to [outFileName].
  static Future<String> encrypt({
    @required String inFilename,
    @required String outFileName,
  }) =>
      _channel.invokeMethod<String>(
        'encrypt',
        <String, String>{
          'inFileName': inFilename,
          'outFileName': outFileName,
        },
      );

  /// Decrypts file in [inFileName] to [outFileName].
  static Future<void> decrypt({
    @required String key,
    @required String inFilename,
    @required String outFileName,
  }) =>
      _channel.invokeMethod<void>(
        'decrypt',
        <String, String>{
          'key': key,
          'inFileName': inFilename,
          'outFileName': outFileName,
        },
      );
}
