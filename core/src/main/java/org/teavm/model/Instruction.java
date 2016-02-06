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
package org.teavm.model;

import org.teavm.model.instructions.InstructionVisitor;

/**
 *
 * @author Alexey Andreev
 */
public abstract class Instruction {
    private BasicBlock basicBlock;
    private InstructionLocation location;

    void setBasicBlock(BasicBlock basicBlock) {
        this.basicBlock = basicBlock;
    }

    public BasicBlock getBasicBlock() {
        return basicBlock;
    }

    public Program getProgram() {
        return basicBlock != null ? basicBlock.getProgram() : null;
    }

    public InstructionLocation getLocation() {
        return location;
    }

    public void setLocation(InstructionLocation location) {
        this.location = location;
    }

    public abstract void acceptVisitor(InstructionVisitor visitor);
}
