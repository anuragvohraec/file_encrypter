# File Encrypter

[![pub package](https://img.shields.io/pub/vpre/file_encrypter.svg)](https://pub.dartlang.org/packages/file_encrypter)

Super fast file encrypter for flutter. Uses AES-256 in CBC mode with PKCS5 padding.

Supports large files too.

**Supports Android Only for now.** Will be working for iOS soon (Contribution is will appreciated).

# Usage
For Encryption:
```dart
String secretKey = await FileEncrypter.encrypt(
    inFilename: 'open_file.mkv',
    outFileName: 'encrypted_file.dat',
); // key is base64 encoded secret key (PRNG generated).

//Creates `encryted_file.dat`, which is encrypted version of `open_file.mkv`.
```

For Decryption:
```dart
String key = await FileEncrypter.decrypt(
    key: secretKey, // secretKey from above
    inFilename: 'encrypted_file.dat',
    outFileName: 'open_file.mkv',
);

//Creates `open_file.mkv`, which is decrypted version of `encrypted_file.dat`.
```


## License

```
Copyright 2020 Sarbagya Dhaubanjar. All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted provided that the following conditions are met:

    * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above
      copyright notice, this list of conditions and the following
      disclaimer in the documentation and/or other materials provided
      with the distribution.
    * Neither the name of Google Inc. nor the names of its
      contributors may be used to endorse or promote products derived
      from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
```
