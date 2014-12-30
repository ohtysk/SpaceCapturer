/*
 * Copyright 2014 Google Inc. All Rights Reserved.

 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package info.doyeah.api;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by cjr on 6/18/14.
 */
public final class Floor {
    private static final float[] COORDS = new float[] {
            200f, 0, -200f,
            -200f, 0, -200f,
            -200f, 0, 200f,
            200f, 0, -200f,
            -200f, 0, 200f,
            200f, 0, 200f,
    };

    private static final float[] NORMALS = new float[] {
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
    };

    private static final float[] COLORS = new float[] {
            0.0f, 0.3398f, 0.9023f, 1.0f,
            0.0f, 0.3398f, 0.9023f, 1.0f,
            0.0f, 0.3398f, 0.9023f, 1.0f,
            0.0f, 0.3398f, 0.9023f, 1.0f,
            0.0f, 0.3398f, 0.9023f, 1.0f,
            0.0f, 0.3398f, 0.9023f, 1.0f,
    };
    private static final FloatBuffer vertices;
    private static final FloatBuffer colors;
    private static final FloatBuffer normals;
    static {
        ByteBuffer bbFloorVertices = ByteBuffer.allocateDirect(COORDS.length * 4);
        bbFloorVertices.order(ByteOrder.nativeOrder());
        vertices = bbFloorVertices.asFloatBuffer();
        vertices.put(COORDS);
        vertices.position(0);

        ByteBuffer bbFloorNormals = ByteBuffer.allocateDirect(NORMALS.length * 4);
        bbFloorNormals.order(ByteOrder.nativeOrder());
        normals = bbFloorNormals.asFloatBuffer();
        normals.put(NORMALS);
        normals.position(0);

        ByteBuffer bbFloorColors = ByteBuffer.allocateDirect(COLORS.length * 4);
        bbFloorColors.order(ByteOrder.nativeOrder());
        colors = bbFloorColors.asFloatBuffer();
        colors.put(COLORS);
        colors.position(0);
    }
    public static FloatBuffer getVertices() {
    	return vertices;
    }
    public static FloatBuffer getColors() {
    	return colors;
    }
    public static FloatBuffer getNormals() {
    	return normals;
    }
}
