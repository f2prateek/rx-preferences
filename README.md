Rx Preferences
--------------

Reactive `SharedPreferences` for Android.


Usage
-----

Create an `RxSharedPreferences` instance which wraps a `SharedPreferences`:

```java
SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
RxSharedPreferences rxPreferences = RxSharedPreferences.create(preferences);
```

*Hint: Keep a strong reference on your `RxSharedPreferences` instance for as long as you want to observe them to prevent listeners from being GCed.*

Create individual `Preference` objects:

```java
Preference<String> username = rxPreferences.getString("username");
Preference<Boolean> showWhatsNew = rxPreferences.getBoolean("show-whats-new", true);
```

Observe changes to individual preferences:

```java
username.asObservable().subscribe(new Action1<String>() {
  @Override public void call(String username) {
    Log.d(TAG, "Username: " + username);
  }
}
```

Subscribe preferences to streams to store values:

```java
RxCompoundButton.checks(showWhatsNewView)
    .subscribe(showWhatsNew.asConsumer());
```
*(Note: `RxCompoundButton` is from [RxBinding][1])*


Download
--------

```groovy
implementation 'com.f2prateek.rx.preferences2:rx-preferences:2.0.1'
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



 [1]: https://github.com/JakeWharton/RxBinding
 [2]: http://repository.sonatype.org/service/local/artifact/maven/redirect?r=central-proxy&g=com.f2prateek.rx.preferences&a=rx-preferences&v=LATEST
