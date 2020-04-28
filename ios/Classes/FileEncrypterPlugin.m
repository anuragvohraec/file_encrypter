#import "FileEncrypterPlugin.h"
#if __has_include(<file_encrypter/file_encrypter-Swift.h>)
#import <file_encrypter/file_encrypter-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "file_encrypter-Swift.h"
#endif

@implementation FileEncrypterPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftFileEncrypterPlugin registerWithRegistrar:registrar];
}
@end
