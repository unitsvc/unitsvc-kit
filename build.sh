#!/bin/sh

#ssh-add ~/.ssh/id_rsa_unitsvc
ssh-add ~/.ssh/id_rsa

# brew install gnupg pinentry-mac
# gpg --gen-key
# gpg --list-keys
# gpg --keyserver keys.openpgp.org --send-keys 3AE3D2BEB09E5C4AF61ADC10D7A05C665AE3A70D
# gpg --keyserver keys.openpgp.org --recv-keys 3AE3D2BEB09E5C4AF61ADC10D7A05C665AE3A70D
# gpg --list-signatures --keyid-format 0xshort

#gpg --list-signatures --keyid-format 0xshort
#[keyboxd]
#---------
#pub   ed25519/0x5AE3A70D 2023-11-28 [SC] [有效至：2026-11-27]
#      3AE3D2BEB09E5C4AF61ADC10D7A05C665AE3A70D
#uid             [ 绝对 ] unitsvc <unitsvc@gmail.com>
#sig 3        0x5AE3A70D 2023-11-28  [自签名]
#sub   cv25519/0xE4287A9A 2023-11-28 [E] [有效至：2026-11-27]
#sig          0x5AE3A70D 2023-11-28  [自签名]

# https://central.sonatype.org/publish/publish-maven/#deployment
mvn clean deploy verify

