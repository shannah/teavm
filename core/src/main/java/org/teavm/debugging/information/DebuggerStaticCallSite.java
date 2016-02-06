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
package org.teavm.debugging.information;

import org.teavm.model.MethodReference;

/**
 *
 * @author Alexey Andreev
 */
public class DebuggerStaticCallSite extends DebuggerCallSite {
    private MethodReference method;

    DebuggerStaticCallSite(MethodReference method) {
        this.method = method;
    }

    public MethodReference getMethod() {
        return method;
    }

    @Override
    public void acceptVisitor(DebuggerCallSiteVisitor visitor) {
        visitor.visit(this);
    }
}
