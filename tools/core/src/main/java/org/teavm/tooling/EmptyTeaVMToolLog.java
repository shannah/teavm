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
package org.teavm.tooling;

/**
 *
 * @author Alexey Andreev
 */
public class EmptyTeaVMToolLog implements TeaVMToolLog {
    @Override
    public void info(String text) {
    }

    @Override
    public void debug(String text) {
    }

    @Override
    public void warning(String text) {
    }

    @Override
    public void error(String text) {
    }

    @Override
    public void info(String text, Throwable e) {
    }

    @Override
    public void debug(String text, Throwable e) {
    }

    @Override
    public void warning(String text, Throwable e) {
    }

    @Override
    public void error(String text, Throwable e) {
    }
}
