#/bin/sh

grep -rl 'RemoteMethodsInterface' ./ | xargs sed -i 's/RemoteMethodsInterface/RemoteMethodsInterface/g'

grep -rl 'MyClientCallback' ./ | xargs sed -i 's/MyClientCallback/MyClientCallback/g'
