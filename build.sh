#!/bin/sh

#ssh-add ~/.ssh/id_rsa_unitsvc
ssh-add ~/.ssh/id_rsa

# brew install gnupg pinentry-mac
# gpg --gen-key
# gpg --list-keys
# gpg --keyserver keys.openpgp.org --send-keys C9C42BDC402813488F028AFAF7F56B4047223A13
# gpg --keyserver keys.openpgp.org --recv-keys C9C42BDC402813488F028AFAF7F56B4047223A13
# gpg --list-signatures --keyid-format 0xshort
mvn clean deploy verify
