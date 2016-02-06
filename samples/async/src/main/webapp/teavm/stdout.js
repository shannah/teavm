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

function $rt_putStdout(ch) {
    if (ch == 0xA) {
        var lineElem = document.createElement("div");
        var stdoutElem = document.getElementById("stdout");
        lineElem.appendChild(document.createTextNode($rt_stdoutBuffer));
        stdoutElem.appendChild(lineElem);
        $rt_stdoutBuffer = "";
        stdoutElem.scrollTop = stdoutElem.scrollHeight;
    } else {
        $rt_stdoutBuffer += String.fromCharCode(ch);
    }
} 
