Rx Preferences
--------------

Reactive SharedPreferences for Android


Usage
-----

To observe all changes to SharedPreferences directly.

```java
SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
SharedPreferencesObservable.observe(sharedPreferences) //
              ...
              .subscribe(new EndlessObserver<String>() {
                @Override public void onNext(String key) {
                  subscriber.onNext(key);
                }
              })

```

You'll most likely want to use the typed preferences instead. Currently `boolean`, `string`, `int`
and `float` types are included.

```java
SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
BooleanPreference booleanPreference = new BooleanPreference(sharedPreferences, "foo");
booleanPreference.asObservable().subscribe(new Observer<Boolean>() {
      @Override public void onCompleted() {

      }

      @Override public void onError(Throwable e) {

      }

      @Override public void onNext(Boolean value) {
          // do something here!
      }
    });
```

Download
--------

Download [the latest JAR][2] or grab via Maven:

```xml
<dependency>
  <groupId>com.f2prateek.rx.preferences</groupId>
  <artifactId>rx-preferences</artifactId>
  <version>1.0.0</version>
</dependency>
```
or Gradle:
```groovy
compile 'com.f2prateek.rx.preferences:rx-preferences:1.0.0'
```


License
-------

    Copyright 2014 Prateek Srivastava

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.



 [1]: http://github.com/f2prateek/rx-preferences
 [2]: http://repository.sonatype.org/service/local/artifact/maven/redirect?r=central-proxy&g=com.f2prateek.rx.preferences&a=rx-preferences&v=LATEST
