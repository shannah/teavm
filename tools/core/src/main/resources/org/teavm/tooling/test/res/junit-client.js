/*
 *  Copyright 2016 "Alexey Andreev"
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

"use strict";
var JUnitClient = {};
JUnitClient.run = function() {
    var handler = function(event) {
        switch (event.data.command) {
            case "runTest":
                window.removeEventListener("message", handler);
                $rt_startThread(function() {
                    JUnitClient.runTest();
                });
                break;
        }
    };
    window.addEventListener("message", handler);
    window.parent.postMessage("ready", "*");
};
JUnitClient.runTest = function() {
    var thread = $rt_nativeThread();
    var instance;
    var ptr = 0;
    var message;
    if (thread.isResuming()) {
        ptr = thread.pop();
        instance = thread.pop();
    }
    loop: while (true) { switch (ptr) {
    case 0:
        try {
            runTest();
        } catch (e) {
            message = {};
            JUnitClient.makeErrorMessage(message, e);
            break loop;
        }
        if (thread.isSuspending()) {
            thread.push(instance);
            thread.push(ptr);
            return;
        }
        message = {};
        message.status = "ok";
        break loop;
    }}
    window.parent.postMessage(message, "*");
};
JUnitClient.makeErrorMessage = function(message, e) {
    message.status = "exception";
    var stack = e.stack;
    if (e.$javaException && e.$javaException.constructor.$meta) {
        message.exception = e.$javaException.constructor.$meta.name;
        message.stack = e.$javaException.constructor.$meta.name + ": ";
        var exceptionMessage = extractException(e.$javaException);
        message.stack += exceptionMessage ? $rt_ustr(exceptionMessage) : "";
    }
    message.stack += "\n" + stack;
};
JUnitClient.reportError = function(error) {
    var handler = function() {
        window.removeEventListener("message", handler);
        var message = { status : "exception", stack : error };
        window.parent.postMessage(message, "*");
    };
    window.addEventListener("message", handler);
};
JUnitClient.loadScript = function(scriptPath) {
    var script = document.createElement("script");
    script.src = scriptPath;
    document.body.appendChild(script);
};
window.addEventListener("message", function(event) {
    var data = event.data;
    switch (data.command) {
        case "loadScript":
            JUnitClient.loadScript(data.script);
            break;
    }
});
window.parent.postMessage("loaded", "*");