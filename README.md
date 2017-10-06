# Android Logging Library

[![Open Source Love](https://badges.frapsoft.com/os/v1/open-source.svg?v=103)](https://opensource.org/licenses/MIT) [![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Download](https://api.bintray.com/packages/piyushgupta27/maven/smart-scheduler/images/download.svg) ](https://bintray.com/piyushgupta27/maven/smart-scheduler/_latestVersion)
## Overview
A utility logger library for Android on top of standard Android `Log` class for debugging purpose. This is a simple library that will allow Android apps or library to store `log` into `database` so that developer can pull the logs from the database into `File` or push the logs to their `server` for `debugging`.

<p align="center">
<kbd>
<img src="asset/device_logger.gif" alt="Device Logger" width="380" height="633">
</kbd>
</p>

## Log Format
```
timeStamp + " | " + appVersion + " : " + osVersion + " | " + deviceUUID + " | [" + logLevelName + "]: " + message
```
```
2017-10-05T14:46:36.541Z 1.0 : Android-7.1.1 | 62bb1162466c3eed | [INFO]: Log has been pushed
```
## Download
Download the latest version or grab via Gradle.

The library is available on `mavenCentral()` and `jcenter()`. In your module's `build.gradle`, add the following code snippet and run the gradle-sync.


```
dependencies {
    ...
    compile 'io.hypertrack:smart-scheduler:0.0.8'
    ...
}
```

## Initialize
Inside `onCreate` of Application class or Launcher Activity. 
```
SmartLog.initialize(this);
SmartLog.setLogLevel(Log.VERBOSE);
```

## Usage
```
SmartLog.d(TAG,"Debug Log");
```

## Get Logs in a File
```
File file = SmartLog.getDeviceLogsInFile(this);
```

## Push Logs to Server
Set the API Endpoint URL `SmartLog.setURL` before calling `SmartLog.pushLogs` method otherwise `exception` will be thrown.
```
SmartLog.setURL("API URL");
SmartLog.pushLogs(this, false, new SmartLogCallback() {
            @Override
            public void onSuccess(@NonNull String response) {

            }

            @Override
            public void onError(@NonNull VolleyError errorResponse) {

            }
});
```
**Endpoint for testing**

<p align="center">
<kbd>
<img src="asset/request_bin.png" alt="RequestBin">
</kbd>
</p>

Use [`RequestBin`](https://requestb.in/) to push the logs.
* Visit the [`RequestBin`](https://requestb.in/) site and create a `RequestBin`.
* Once you have the bin created, copy the URL and set it to the `SmartLog.setURL`.
* After `SmartLog.pushLogs` reload the related view page to view exactly which requests were made, what headers were sent, and raw body and so on, all in a pretty graphical setting.
* Once you get the logs on `RequestBin` create your own endpoint on your server and start receiving logs on to your server for debugging.

**Note:** 
* Push logs to server in compressed form to reduce the data consumption and response time.
```
SmartLog.pushLogs(Context mContext, boolean compress, SmartLogCallback callback);
```
* Logs will be compressed using `GZIP` encoding.
* Logs will be deleted from database after successful push.
* Logs will push to the server in batches. Each batch can have maximum of `5000 logs`.

## Additional Methods
* Different types of log.
```
SmartLog.d(TAG,"debug");
SmartLog.i(TAG,"information");
SmartLog.e(TAG,"error");
SmartLog.v(TAG,"verbose");
SmartLog.w(TAG,"warning");
SmartLog.a(TAG,"assert");
SmartLog.exception(TAG,"exception",throwable);
```

* To check whether any device logs are available.
```
SmartLog.hasPendingDeviceLogs();
```

* Get the count of stored device logs.
```
SmartLog.logCount();
```

* Developer can pass additional headers along with API call while pushing logs to server.
```
HashMap<String, String> additionalHeaders = new HashMap<>();
additionalHeaders.put("Authorization","Token");
SmartLog.pushLogs(this, additionalHeaders, false, smartLogCallback);
```

* By default, seven days older logs will get delete automatically from the database. You can change the expiry period of logs by defining `expiryTimeInSeconds`.
```
SmartLog.initialize(@NonNull Context context, int expiryTimeInSeconds);
```
* Developers can also get the device log as a list of `DeviceLog` model or list of `String` .By default, fetched logs will delete from the database. Developers can override to change the default functionality.
```
SmartLog.getDeviceLogs(boolean deleteLogs);
SmartLog.getDeviceLogsInFile(Context mContext, boolean deleteLogs);
```
* By default, every get calls return data from first batch if there are one or more batch.
* If there are more than one batches then developer can gets the specific batch data by providing batch number.
```
SmartLog.getDeviceLogs(boolean deleteLogs, int batchNo);
```
* Get the number of batches using `SmartLog.getDeviceLogBatchCount`.
* Developer can manually delete the logs `SmartLog.deleteLogs`.

## Contribute
Please use the [issues tracker](https://github.com/hypertrack/smart-logging-android/issues) to raise bug reports and feature requests. We'd love to see your pull requests, so send them in!

## About HyperTrack
Developers use [HyperTrack](https://www.hypertrack.com) to build location features, not infrastructure. We reduce the complexity of building and operating location features to a few APIs that just work.
Check it out. [Sign up](https://dashboard.hypertrack.com/signup/) and start building! Join our [Slack community](http://slack.hypertrack.io) for instant responses. You can also email us at help@hypertrack.io

## License

```
MIT License

Copyright (c) 2016 HyperTrack

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
