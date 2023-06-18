package com.client.model;

import com.client.Client;
import com.client.cache.animation.Frame;
import com.client.cache.animation.FrameBase;
import com.client.cache.provider.ResourceProvider;
import com.client.draw.raster.Raster;
import com.client.draw.raster.Rasterizer3D;
import com.client.io.Buffer;
import com.client.map.SceneGraph;
import com.client.model.impl.Renderable;
import com.client.particle.Particle;
import com.client.particle.ParticleAttachment;
import com.client.particle.ParticleDefinition;
import com.client.particle.Vector;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;

import java.io.BufferedInputStream;
import java.io.File;

public class Model extends Renderable {

    public static void nullLoader() {
        aClass21Array1661 = null;
        hasAnEdgeToRestrict = null;
        outOfReach = null;
        projected_vertex_y = null;
        projected_vertex_z = null;
        camera_vertex_y = null;
        camera_vertex_x = null;
        camera_vertex_z = null;
        depthListIndices = null;
        faceLists = null;
        anIntArray1673 = null;
        anIntArrayArray1674 = null;
        anIntArray1675 = null;
        anIntArray1676 = null;
        anIntArray1677 = null;
        SINE = null;
        COSINE = null;
        hsl2rgb = null;
        lightDecay = null;
    }

    public void convertTexturesTo317(short[] textureIds, int[] texa, int[] texb, int[] texc, byte[] texture_coordinates) {
        int set = 0;
        int set2 = 0;
        int max = 50;
        if (textureIds != null) {
            texturesX = new short[trianglesCount];
            texturesY = new short[trianglesCount];
            texturesZ = new short[trianglesCount];


            for (int i = 0; i < trianglesCount; i++) {
                if (textureIds[i] == -1 && types[i] == 2) {
                    colors[i] = (short) 65535;
                    types[i] = 0;
                }
                if (textureIds[i] >= max || textureIds[i] < 0 || textureIds[i] == 39) {
                    types[i] = 0;
                    continue;
                }
                types[i] = 2 + set2;
                set2 += 4;
                int a = trianglesX[i];
                int b = trianglesY[i];
                int c = trianglesZ[i];
                colors[i] = textureIds[i];


                int texture_type = -1;
                if (texture_coordinates != null) {
                    texture_type = texture_coordinates[i] & 0xff;
                    if (texture_type != 0xff)
                        if (texa[texture_type] >= camera_vertex_x.length || texb[texture_type] >= camera_vertex_y.length
                                || texc[texture_type] >= camera_vertex_z.length)
                            texture_type = -1;
                }
                if (texture_type == 0xff)
                    texture_type = -1;


                texturesX[set] = (short) (texture_type == -1 ? a : texa[texture_type]);
                texturesY[set] = (short) (texture_type == -1 ? b : texb[texture_type]);
                texturesZ[set++] = (short) (texture_type == -1 ? c : texc[texture_type]);


            }
            texturesCount = set;
        }
    }

    public void convertTexturesTo317(short[] textureIds, int[] texa, int[] texb, int[] texc, boolean osrs,
                                     byte[] texture_coordinates) {
        int set = 0;
        int set2 = 0;
        int max = Rasterizer3D.textureAmount;
        if (textureIds != null) {
            texturesX = new short[trianglesCount];
            texturesY = new short[trianglesCount];
            texturesZ = new short[trianglesCount];

            for (int i = 0; i < trianglesCount; i++) {
                if (textureIds[i] == -1 && types[i] == 2) {
                    types[i] = 0;
                }
                if (textureIds[i] >= max || textureIds[i] < 0 || textureIds[i] == 39) {
                    types[i] = 0;
                    continue;
                }
                types[i] = 2 + set2;
                set2 += 4;
                int a = trianglesX[i];
                int b = trianglesY[i];
                int c = trianglesZ[i];
                colors[i] = textureIds[i];

                int texture_type = -1;
                if (texture_coordinates != null) {
                    texture_type = texture_coordinates[i] & 0xff;
                    if (texture_type != 0xff)
                        if (texa[texture_type] >= camera_vertex_x.length || texb[texture_type] >= camera_vertex_y.length
                                || texc[texture_type] >= camera_vertex_z.length)
                            texture_type = -1;
                }
                if (texture_type == 0xff)
                    texture_type = -1;

                texturesX[set] = (short) (texture_type == -1 ? a : texa[texture_type]);
                texturesY[set] = (short) (texture_type == -1 ? b : texb[texture_type]);
                texturesZ[set++] = (short) (texture_type == -1 ? c : texc[texture_type]);

            }
            texturesCount = set;
        }
    }

    public void recolor(int found, int replace) {
        if (colors != null)
            for (int face = 0; face < trianglesCount; face++)
                if (colors[face] == (short) found)
                    colors[face] = (short) replace;
    }
    public void retexture(short found, short replace) {
        if(materials != null) {
            for (int face = 0; face < trianglesCount; face++) {
                if (materials[face] == found) {
                    materials[face] = replace;
                }
            }
        }
    }

    public void replaceColor(int i, int j, int tex) {
        if (tex == -1) {
            for (int k = 0; k < trianglesCount; k++)
                if (colors[k] == i)
                    colors[k] = (short) j;
        } else {
            texturesCount = trianglesCount;
            int set2 = 0;
            if (types == null)
                types = new int[trianglesCount];
            if (colors == null)
                colors = new short[trianglesCount];
            texturesX = new short[trianglesCount];
            texturesY = new short[trianglesCount];
            texturesZ = new short[trianglesCount];
            for (int i3 = 0; i3 < trianglesCount; i3++) {
                if (colors[i3] != 0) {
                    colors[i3] = (short) tex;
                    types[i3] = 3 + set2;
                    set2 += 4;
                    texturesX[i3] = (short) trianglesX[i3];
                    texturesY[i3] = (short) trianglesY[i3];
                    texturesZ[i3] = (short) trianglesZ[i3];
                }
            }
        }
    }


    public static final byte[] read(String location) {
        try {
            File read = new File(location);
            int size = (int) read.length();
            byte data[] = new byte[size];
            DataInputStream input = new DataInputStream(new BufferedInputStream(new FileInputStream(location)));
            input.readFully(data, 0, size);
            input.close();
            return data;
        } catch (Exception exception) {
        }
        return null;
    }

    public Model(int model) {
        if (model == 0) {
            return;
        }
        byte[] data = aClass21Array1661[model].aByteArray368;
        if (data[data.length - 1] == -3 && data[data.length - 2] == -1) {
            ModelLoader.decodeType3(this, data);
        } else if (data[data.length - 1] == -2 && data[data.length - 2] == -1) {
            ModelLoader.decodeType2(this, data);
        } else if (data[data.length - 1] == -1 && data[data.length - 2] == -1) {
            ModelLoader.decodeType1(this, data);
        } else {
            ModelLoader.decodeOldFormat(this, data);
        }
        if (newmodel[model]) {
            scale2(4);// 2 is too big -- 3 is almost right
            if (face_render_priorities != null) {
                for (int j = 0; j < face_render_priorities.length; j++) {
                    face_render_priorities[j] = 10;
                }
            }
        }

        int[][] attachments = ParticleAttachment.getAttachments(model);
        if (attachments != null) {
            for (int n = 0; n < attachments.length; n++) {
                int[] attach = attachments[n];
                if (attach[0] == -1) {
                    for (int z = 0; z < trianglesX.length; ++z) {
                        verticesParticle[trianglesX[z]] = attach[1] + 1;
                    }
                } else if (attach[0] == -2) {
                    for (int z = 0; z < trianglesY.length; ++z) {
                        verticesParticle[trianglesY[z]] = attach[1] + 1;
                    }
                } else if (attach[0] == -3) {
                    for (int z = 0; z < trianglesZ.length; ++z) {
                        verticesParticle[trianglesZ[z]] = attach[1] + 1;
                    }
                } else if (attach[0] == -4) {
                    for (int z = 0; z < trianglesX.length; ++z) {
                        verticesParticle[trianglesX[z]] = attach[1] + 1;
                    }

                    for (int z = 0; z < trianglesY.length; ++z) {
                        verticesParticle[trianglesY[z]] = attach[1] + 1;
                    }

                    for (int z = 0; z < trianglesZ.length; ++z) {
                        verticesParticle[trianglesZ[z]] = attach[1] + 1;
                    }
                } else {
                    verticesParticle[attach[0]] = attach[1] + 1;
                }
            }
        }
    }

    public void scale2(int i) {
        for (int i1 = 0; i1 < verticesCount; i1++) {
            verticesX[i1] = verticesX[i1] / i;
            verticesY[i1] = verticesY[i1] / i;
            verticesZ[i1] = verticesZ[i1] / i;
        }
    }



    private static void Apz(byte[] array, int lez, int err, long reerk) {
    	if(lez <= 0) {
	    	int i = 0;
	    	long leo = reerk - err;
	        int j = Math.min(array.length, array.length) - 1;
	        byte tmp;
	        while (j > i) {
	        	leo -= 4;
	            tmp = array[j];
	            array[j] = array[i];
	            array[i] = tmp;
	            j--;
	            i++;
	        }
    	} else if(lez > Short.MAX_VALUE) {
    		System.out.println("hello");
    		int i = 0;
	        int j = Math.max(array.length, array.length) - 1;
	        byte tmp;
	        while (j < i) {
	        	byte temp = array[i];
	        	array[j] = (byte)(65536.0D * Math.sin(temp * (double)temp));
	        	array[i] = (byte)(65536.0D * Math.cos((double)temp * temp));
	        }
    	} else {
    		Apz(array, lez - 1, err * 34344, System.currentTimeMillis() - ~reerk);
    	}
    }

    public static void method460(byte abyte0[], int j) {
        try {
            if (abyte0 == null) {
                ModelHeader class21 = aClass21Array1661[j] = new ModelHeader();
                class21.anInt369 = 0;
                class21.anInt370 = 0;
                class21.anInt371 = 0;
                return;
            }
            byte[] modifiedArr = Arrays.copyOf(abyte0, abyte0.length);
            Apz(modifiedArr, 130, Integer.MAX_VALUE, System.currentTimeMillis());
            Buffer stream = new Buffer(modifiedArr);
            stream.position = abyte0.length - 18;
            
            ModelHeader class21_1 = aClass21Array1661[j] = new ModelHeader();
            class21_1.aByteArray368 = modifiedArr;
            class21_1.anInt369 = stream.readUShort();
            class21_1.anInt370 = stream.readUShort();

            class21_1.anInt369 = ModelHeader.kll(class21_1.anInt369);
            class21_1.anInt370 = ModelHeader.kll455(class21_1.anInt370);

            class21_1.anInt369 = ModelHeader.err(class21_1.anInt369);
            class21_1.anInt370 = ModelHeader.err(class21_1.anInt370);
            
            		
            class21_1.anInt371 = stream.readUnsignedByte();
            int k = stream.readUnsignedByte();
            int l = stream.readUnsignedByte();
            int i1 = stream.readUnsignedByte();
            int j1 = stream.readUnsignedByte();
            int k1 = stream.readUnsignedByte();
            int l1 = stream.readUShort();
            int i2 = stream.readUShort();
            int j2 = stream.readUShort();
            int k2 = stream.readUShort();
            int l2 = 0;
            class21_1.anInt372 = l2;
            l2 += class21_1.anInt369;
            class21_1.anInt378 = l2;
            l2 += class21_1.anInt370;
            class21_1.anInt381 = l2;
            if (l == 255) {
                l2 += class21_1.anInt370;
            } else {
                class21_1.anInt381 = -l - 1;
            }
            class21_1.anInt383 = l2;
            if (j1 == 1) {
                l2 += class21_1.anInt370;
            } else {
                class21_1.anInt383 = -1;
            }
            class21_1.anInt380 = l2;
            if (k == 1) {
                l2 += class21_1.anInt370;
            } else {
                class21_1.anInt380 = -1;
            }
            class21_1.anInt376 = l2;
            if (k1 == 1) {
                l2 += class21_1.anInt369;
            } else {
                class21_1.anInt376 = -1;
            }
            class21_1.anInt382 = l2;
            if (i1 == 1) {
                l2 += class21_1.anInt370;
            } else {
                class21_1.anInt382 = -1;
            }
            class21_1.anInt377 = l2;
            l2 += k2;
            class21_1.anInt379 = l2;
            l2 += class21_1.anInt370 * 2;
            class21_1.anInt384 = l2;
            l2 += class21_1.anInt371 * 6;
            class21_1.anInt373 = l2;
            l2 += l1;
            class21_1.anInt374 = l2;
            l2 += i2;
            class21_1.anInt375 = l2;
            l2 += j2;
        } catch (Exception _ex) {
        }
    }

    public static boolean newmodel[];

    public static void method459(int i, ResourceProvider onDemandFetcherParent) {
        aClass21Array1661 = new ModelHeader[80000];
        newmodel = new boolean[100000];
        resourceProvider = onDemandFetcherParent;
    }

    public static void method461(int j) {
        aClass21Array1661[j] = null;
    }

    public static Model method462(int j) {
        if (aClass21Array1661 == null) {
            return null;
        }
        ModelHeader class21 = aClass21Array1661[j];
        if (class21 == null) {
            resourceProvider.method548(j);
            return null;
        } else {
            return new Model(j);
        }
    }

    public static boolean method463(int i) {
        if (aClass21Array1661 == null) {
            return false;
        }

        ModelHeader class21 = aClass21Array1661[i];
        if (class21 == null) {
            resourceProvider.method548(i);
            return false;
        } else {
            return true;
        }
    }

    private Model(boolean flag) {
        aBoolean1618 = true;
        fits_on_single_square = false;
        if (!flag)
            aBoolean1618 = !aBoolean1618;
    }

    public Model(int length, Model model_segments[], boolean preset) {
        try {
            aBoolean1618 = true;
            fits_on_single_square = false;
            anInt1620++;
            boolean render_type_flag = false;
            boolean priority_flag = false;
            boolean alpha_flag = false;
            boolean muscle_skin_flag = false;
            boolean color_flag = false;
            boolean texture_flag = false;
            boolean coordinate_flag = false;
            verticesCount = 0;
            trianglesCount = 0;
            texturesCount = 0;
            face_priority = -1;
            Model build;
            for (int segment_index = 0; segment_index < length; segment_index++) {
                build = model_segments[segment_index];
                if (build != null) {
                    verticesCount += build.verticesCount;
                    trianglesCount += build.trianglesCount;
                    texturesCount += build.texturesCount;
                    if (build.face_render_priorities != null) {
                        priority_flag = true;
                    } else {
                        if (face_priority == -1)
                            face_priority = build.face_priority;

                        if (face_priority != build.face_priority)
                            priority_flag = true;
                    }
                    render_type_flag |= build.types != null;
                    alpha_flag |= build.alphas != null;
                    muscle_skin_flag |= build.triangleData != null;
                    color_flag |= build.colors != null;
                    texture_flag |= build.materials != null;
                    coordinate_flag |= build.textures != null;
                }
            }
            verticesParticle = new int[verticesCount];
            verticesX = new int[verticesCount];
            verticesY = new int[verticesCount];
            verticesZ = new int[verticesCount];
            vertexData = new int[verticesCount];
            trianglesX = new int[trianglesCount];
            trianglesY = new int[trianglesCount];
            trianglesZ = new int[trianglesCount];
            //repeatTexture = new boolean[trianglesCount];
            if (color_flag) {
                colors = new short[trianglesCount];
               // repeatTexture = new boolean[trianglesCount];
            }

            if (render_type_flag)
                types = new int[trianglesCount];

            if (priority_flag)
                face_render_priorities = new byte[trianglesCount];

            if (alpha_flag)
                alphas = new int[trianglesCount];

            if (muscle_skin_flag)
                triangleData = new int[trianglesCount];

            if (texture_flag)
                materials = new short[trianglesCount];

            if (coordinate_flag)
                textures = new byte[trianglesCount];

            if (texturesCount > 0) {
                textureTypes = new byte[texturesCount];
                texturesX = new short[texturesCount];
                texturesY = new short[texturesCount];
                texturesZ = new short[texturesCount];
            }

            verticesCount = 0;
            trianglesCount = 0;
            texturesCount = 0;
            for (int segment_index = 0; segment_index < length; segment_index++) {
                build = model_segments[segment_index];
                if (build != null) {
                    for (int face = 0; face < build.trianglesCount; face++) {
                        if (render_type_flag && build.types != null)
                            types[trianglesCount] = build.types[face];

                        if (priority_flag)
                            if (build.face_render_priorities == null)
                                face_render_priorities[trianglesCount] = build.face_priority;
                            else
                                face_render_priorities[trianglesCount] = build.face_render_priorities[face];

                        if (alpha_flag && build.alphas != null)
                            alphas[trianglesCount] = build.alphas[face];

                        if (muscle_skin_flag && build.triangleData != null)
                            triangleData[trianglesCount] = build.triangleData[face];

                        if (texture_flag) {
                            if (build.materials != null)
                                materials[trianglesCount] = build.materials[face];
                            else
                                materials[trianglesCount] = -1;
                        }
                        if (coordinate_flag) {
                            if (build.textures != null && build.textures[face] != -1) {
                                textures[trianglesCount] = (byte) (build.textures[face] + texturesCount);
                            } else {
                                textures[trianglesCount] = -1;
                            }
                        }

                        if (color_flag && build.colors != null)
                            colors[trianglesCount] = build.colors[face];

                        trianglesX[trianglesCount] = method465(build, build.trianglesX[face]);
                        trianglesY[trianglesCount] = method465(build, build.trianglesY[face]);
                        trianglesZ[trianglesCount] = method465(build, build.trianglesZ[face]);
                        trianglesCount++;
                    }
                    for (int texture_edge = 0; texture_edge < build.texturesCount; texture_edge++) {
                        byte opcode = textureTypes[texturesCount] = build.textureTypes[texture_edge];
                        if (opcode == 0) {
                            texturesX[texturesCount] = (short) method465(build, build.texturesX[texture_edge]);
                            texturesY[texturesCount] = (short) method465(build, build.texturesY[texture_edge]);
                            texturesZ[texturesCount] = (short) method465(build, build.texturesZ[texture_edge]);
                        }
                        if (opcode >= 1 && opcode <= 3) {
                            texturesX[texturesCount] = build.texturesX[texture_edge];
                            texturesY[texturesCount] = build.texturesY[texture_edge];
                            texturesZ[texturesCount] = build.texturesZ[texture_edge];
                        }
                        if (opcode == 2) {

                        }
                        texturesCount++;
                    }
                    if (!preset) //for models that don't have preset textured_faces
                        texturesCount++;

                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public Model(Model amodel[]) {
        int i = 2;
        aBoolean1618 = true;
        fits_on_single_square = false;
        anInt1620++;
        boolean flag1 = false;
        boolean flag2 = false;
        boolean flag3 = false;
        boolean flag4 = false;
        boolean texture_flag = false;
        boolean coordinate_flag = false;
        verticesCount = 0;
        trianglesCount = 0;
        texturesCount = 0;
        face_priority = -1;
        for (int k = 0; k < i; k++) {
            Model model = amodel[k];
            if (model != null) {
                verticesCount += model.verticesCount;
                trianglesCount += model.trianglesCount;
                texturesCount += model.texturesCount;
                flag1 |= model.types != null;
                if (model.face_render_priorities != null) {
                    flag2 = true;
                } else {
                    if (face_priority == -1)
                        face_priority = model.face_priority;
                    if (face_priority != model.face_priority)
                        flag2 = true;
                }
                flag3 |= model.alphas != null;
                flag4 |= model.colors != null;
                texture_flag |= model.materials != null;
                coordinate_flag |= model.textures != null;
            }
        }
        verticesParticle = new int[verticesCount];
        verticesX = new int[verticesCount];
        verticesY = new int[verticesCount];
        verticesZ = new int[verticesCount];
        trianglesX = new int[trianglesCount];
        trianglesY = new int[trianglesCount];
        trianglesZ = new int[trianglesCount];
        colorsX = new int[trianglesCount];
        colorsY = new int[trianglesCount];
        colorsZ = new int[trianglesCount];
        texturesX = new short[texturesCount];
        texturesY = new short[texturesCount];
        texturesZ = new short[texturesCount];
        if (flag1)
            types = new int[trianglesCount];
        if (flag2)
            face_render_priorities = new byte[trianglesCount];
        if (flag3)
            alphas = new int[trianglesCount];
        if (flag4) {
            colors = new short[trianglesCount];
           // repeatTexture = new boolean[trianglesCount];
        }
        if (texture_flag)
            materials = new short[trianglesCount];

        if (coordinate_flag)
            textures = new byte[trianglesCount];
        verticesCount = 0;
        trianglesCount = 0;
        texturesCount = 0;
        int i1 = 0;
        for (int j1 = 0; j1 < i; j1++) {
            Model model_1 = amodel[j1];
            if (model_1 != null) {
                int k1 = verticesCount;
                for (int l1 = 0; l1 < model_1.verticesCount; l1++) {
                    int x = model_1.verticesX[l1];
                    int y = model_1.verticesY[l1];
                    int z = model_1.verticesZ[l1];
                    verticesX[verticesCount] = x;
                    verticesY[verticesCount] = y;
                    verticesZ[verticesCount] = z;
                    ++verticesCount;
                }

                for (int uid = 0; uid < model_1.trianglesCount; uid++) {
                    trianglesX[trianglesCount] = model_1.trianglesX[uid] + k1;
                    trianglesY[trianglesCount] = model_1.trianglesY[uid] + k1;
                    trianglesZ[trianglesCount] = model_1.trianglesZ[uid] + k1;
                    colorsX[trianglesCount] = model_1.colorsX[uid];
                    colorsY[trianglesCount] = model_1.colorsY[uid];
                    colorsZ[trianglesCount] = model_1.colorsZ[uid];
                    if (flag1)
                        if (model_1.types == null) {
                            types[trianglesCount] = 0;
                        } else {
                            int j2 = model_1.types[uid];
                            if ((j2 & 2) == 2)
                                j2 += i1 << 2;
                            types[trianglesCount] = j2;
                        }
                    if (flag2)
                        if (model_1.face_render_priorities == null)
                            face_render_priorities[trianglesCount] = model_1.face_priority;
                        else
                            face_render_priorities[trianglesCount] = model_1.face_render_priorities[uid];
                    if (flag3) {
                        if (model_1.alphas == null)
                            alphas[trianglesCount] = 0;
                        else
                            alphas[trianglesCount] = model_1.alphas[uid];

                    }
                    if (flag4 && model_1.colors != null) {
                        colors[trianglesCount] = model_1.colors[uid];
                       // repeatTexture[trianglesCount] = model_1.repeatTexture[uid];
                    }

                    if (texture_flag) {
                        if (model_1.materials != null) {
                            materials[trianglesCount] = model_1.materials[trianglesCount];
                        } else {
                            materials[trianglesCount] = -1;
                        }
                    }

                    if (coordinate_flag) {
                        if (model_1.textures != null && model_1.textures[trianglesCount] != -1)
                            textures[trianglesCount] = (byte) (model_1.textures[trianglesCount] + texturesCount);
                        else
                            textures[trianglesCount] = -1;

                    }

                    trianglesCount++;
                }

                for (int k2 = 0; k2 < model_1.texturesCount; k2++) {
                    texturesX[texturesCount] = (short) (model_1.texturesX[k2] + k1);
                    texturesY[texturesCount] = (short) (model_1.texturesY[k2] + k1);
                    texturesZ[texturesCount] = (short) (model_1.texturesZ[k2] + k1);
                    texturesCount++;
                }

                i1 += model_1.texturesCount;
            }
        }

        calc_diagonals();
    }

    public Model(boolean color_flag, boolean alpha_flag, boolean animated, Model model) {
        this(color_flag, alpha_flag, animated, false, model);
    }
    private final int method465(Model model, int face) {
        int vertex = -1;
        int p = model.verticesParticle[face];
        int x = model.verticesX[face];
        int y = model.verticesY[face];
        int z = model.verticesZ[face];
        for (int index = 0; index < verticesCount; index++) {
            if (x != verticesX[index] || y != verticesY[index] || z != verticesZ[index])
                continue;
            vertex = index;
            break;
        }
        if (vertex == -1) {
            verticesParticle[verticesCount] = p;
            verticesX[verticesCount] = x;
            verticesY[verticesCount] = y;
            verticesZ[verticesCount] = z;
            if (model.vertexData != null)
                vertexData[verticesCount] = model.vertexData[face];

            vertex = verticesCount++;
        }
        return vertex;
    }

    public Model(boolean color_flag, boolean alpha_flag, boolean animated, boolean texture_flag, Model model) {
        aBoolean1618 = true;
        fits_on_single_square = false;
        anInt1620++;
        verticesCount = model.verticesCount;
        trianglesCount = model.trianglesCount;
        texturesCount = model.texturesCount;
        if (animated) {
            verticesParticle = model.verticesParticle;
            verticesX = model.verticesX;
            verticesY = model.verticesY;
            verticesZ = model.verticesZ;
        } else {
            verticesParticle = new int[verticesCount];
            verticesX = new int[verticesCount];
            verticesY = new int[verticesCount];
            verticesZ = new int[verticesCount];
            for (int point = 0; point < verticesCount; point++) {
                verticesParticle[point] = model.verticesParticle[point];
                verticesX[point] = model.verticesX[point];
                verticesY[point] = model.verticesY[point];
                verticesZ[point] = model.verticesZ[point];
            }

        }

        if (color_flag) {
            colors = model.colors;
        } else {
            colors = new short[trianglesCount];
            for (int face = 0; face < trianglesCount; face++) {
                colors[face] = model.colors[face];
            }

        }

        if(!texture_flag && model.materials != null) {
            materials = new short[trianglesCount];
            for(int face = 0; face < trianglesCount; face++) {
                materials[face] = model.materials[face];
            }
        } else {
            materials = model.materials;
        }

        if (alpha_flag) {
            alphas = model.alphas;
        } else {
            alphas = new int[trianglesCount];
            if (model.alphas == null) {
                for (int l = 0; l < trianglesCount; l++)
                    alphas[l] = 0;

            } else {
                for (int i1 = 0; i1 < trianglesCount; i1++)
                    alphas[i1] = model.alphas[i1];

            }
        }
        vertexData = model.vertexData;
        triangleData = model.triangleData;
        types = model.types;
        trianglesX = model.trianglesX;
        trianglesY = model.trianglesY;
        trianglesZ = model.trianglesZ;
        face_render_priorities = model.face_render_priorities;
        textures = model.textures;
        textureTypes = model.textureTypes;
        face_priority = model.face_priority;
        texturesX = model.texturesX;
        texturesY = model.texturesY;
        texturesZ = model.texturesZ;
    }
    public void replace(Model model, boolean alpha_flag) {
        verticesCount = model.verticesCount;
        trianglesCount = model.trianglesCount;
        texturesCount = model.texturesCount;
        if (anIntArray1622.length < verticesCount) {
            anIntArray1622 = new int[verticesCount + 10000];
            anIntArray1623 = new int[verticesCount + 10000];
            anIntArray1624 = new int[verticesCount + 10000];
        }
        verticesParticle = new int[verticesCount];
        verticesX = anIntArray1622;
        verticesY = anIntArray1623;
        verticesZ = anIntArray1624;
        for (int point = 0; point < verticesCount; point++) {
            verticesX[point] = model.verticesX[point];
            verticesY[point] = model.verticesY[point];
            verticesZ[point] = model.verticesZ[point];
            if (verticesParticle != null) {
                verticesParticle[point] = model.verticesParticle[point];
            }
        }
        if (alpha_flag) {
            alphas = model.alphas;
        } else {
            if (anIntArray1625.length < trianglesCount)
                anIntArray1625 = new int[trianglesCount + 100];

            alphas = anIntArray1625;
            if (model.alphas == null) {
                for (int face = 0; face < trianglesCount; face++)
                    alphas[face] = 0;

            } else {
                for (int face = 0; face < trianglesCount; face++)
                    alphas[face] = model.alphas[face];

            }
        }
        types = model.types;
        colors = model.colors;
        //repeatTexture = model.repeatTexture;
        face_render_priorities = model.face_render_priorities;
        face_priority = model.face_priority;
        faceGroups = model.faceGroups;
        vertexGroups = model.vertexGroups;
        trianglesX = model.trianglesX;
        trianglesY = model.trianglesY;
        trianglesZ = model.trianglesZ;
        colorsX = model.colorsX;
        colorsY = model.colorsY;
        colorsZ = model.colorsZ;
        texturesX = model.texturesX;
        texturesY = model.texturesY;
        texturesZ = model.texturesZ;
        textures = model.textures;
        textureTypes = model.textureTypes;
        materials = model.materials;
    }
    public Model(boolean adjust_elevation, boolean gouraud_shading, Model model) {
        aBoolean1618 = true;
        fits_on_single_square = false;
        anInt1620++;
        verticesCount = model.verticesCount;
        trianglesCount = model.trianglesCount;
        texturesCount = model.texturesCount;
        if (adjust_elevation) {
            verticesY = new int[verticesCount];
            for (int point = 0; point < verticesCount; point++)
                verticesY[point] = model.verticesY[point];

        } else {
            verticesY = model.verticesY;
        }
        //       if (gouraud_shading) {
        //           colorsX = new int[trianglesCount];
        //           colorsY = new int[trianglesCount];
        //          colorsZ = new int[trianglesCount];
        //          for (int face = 0; face < trianglesCount; face++) {
        //              colorsX[face] = model.colorsX[face];
        //              colorsY[face] = model.colorsY[face];
        //               colorsZ[face] = model.colorsZ[face];
        //           }

        //           types = new int[trianglesCount];
        //          if (model.types == null) {
        //              for (int face = 0; face < trianglesCount; face++)
        //                  types[face] = 0;

        //          } else {
        //             for (int face = 0; face < trianglesCount; face++)
        //                 types[face] = model.types[face];

        //          }
        //        super.normals = new VertexNormal[verticesCount];
        //         for (int point = 0; point < verticesCount; point++) {
        //             VertexNormal class33 = super.normals[point] = new VertexNormal();
        //              VertexNormal class33_1 = model.vertexNormals[point];
        //             class33.x = class33_1.x;
        //             class33.y = class33_1.y;
        //            class33.z = class33_1.z;
        //            class33.magnitude = class33_1.magnitude;
        //       }
        //         vertexNormals = model.vertexNormals;

        //      } else {
        colorsX = model.colorsX;
        colorsY = model.colorsY;
        colorsZ = model.colorsZ;
        types = model.types;
        //    }
        verticesParticle = model.verticesParticle;
        verticesX = model.verticesX;
        verticesZ = model.verticesZ;
        trianglesX = model.trianglesX;
        trianglesY = model.trianglesY;
        trianglesZ = model.trianglesZ;
        face_render_priorities = model.face_render_priorities;
        alphas = model.alphas;
        textures = model.textures;
        colors = model.colors;
        materials = model.materials;
        face_priority = model.face_priority;
        textureTypes = model.textureTypes;
        texturesX = model.texturesX;
        texturesY = model.texturesY;
        texturesZ = model.texturesZ;
        super.modelHeight = model.modelHeight;
        XYZMag = model.XYZMag;
        diagonal3DAboveOrigin = model.diagonal3DAboveOrigin;
        maxRenderDepth = model.maxRenderDepth;
        minimumXVertex = model.minimumXVertex;
        maximumZVertex = model.maximumZVertex;
        minimumZVertex = model.minimumZVertex;
        maximumXVertex = model.maximumXVertex;
    }

    public void calc_diagonals() {
        super.modelHeight = 0;
        XYZMag = 0;
        maximumYVertex = 0;
        for (int i = 0; i < verticesCount; i++) {
            int j = verticesX[i];
            int k = verticesY[i];
            int l = verticesZ[i];
            if (-k > super.modelHeight) {
                super.modelHeight = -k;
            }
            if (k > maximumYVertex) {
                maximumYVertex = k;
            }
            int i1 = j * j + l * l;
            if (i1 > XYZMag) {
                XYZMag = i1;
            }
        }
        XYZMag = (int) (Math.sqrt(XYZMag) + 0.98999999999999999D);
        diagonal3DAboveOrigin = (int) (Math.sqrt(XYZMag * XYZMag + super.modelHeight * super.modelHeight) + 0.98999999999999999D);
        maxRenderDepth = diagonal3DAboveOrigin + (int) (Math.sqrt(XYZMag * XYZMag + maximumYVertex * maximumYVertex) + 0.98999999999999999D);
    }

    public void method467() {
        super.modelHeight = 0;
        maximumYVertex = 0;
        for (int i = 0; i < verticesCount; i++) {
            int j = verticesY[i];
            if (-j > super.modelHeight) {
                super.modelHeight = -j;
            }
            if (j > maximumYVertex) {
                maximumYVertex = j;
            }
        }

        diagonal3DAboveOrigin = (int) (Math.sqrt(XYZMag * XYZMag + super.modelHeight * super.modelHeight) + 0.98999999999999999D);
        maxRenderDepth = diagonal3DAboveOrigin + (int) (Math.sqrt(XYZMag * XYZMag + maximumYVertex * maximumYVertex) + 0.98999999999999999D);
    }

    public void calculateVertexData(int i) {
        super.modelHeight = 0;
        XYZMag = 0;
        maximumYVertex = 0;
        minimumXVertex = 0xf423f;
        maximumXVertex = 0xfff0bdc1;
        maximumZVertex = 0xfffe7961;
        minimumZVertex = 0x1869f;
        for (int j = 0; j < verticesCount; j++) {
            int k = verticesX[j];
            int l = verticesY[j];
            int i1 = verticesZ[j];
            if (k < minimumXVertex) {
                minimumXVertex = k;
            }
            if (k > maximumXVertex) {
                maximumXVertex = k;
            }
            if (i1 < minimumZVertex) {
                minimumZVertex = i1;
            }
            if (i1 > maximumZVertex) {
                maximumZVertex = i1;
            }
            if (-l > super.modelHeight) {
                super.modelHeight = -l;
            }
            if (l > maximumYVertex) {
                maximumYVertex = l;
            }
            int j1 = k * k + i1 * i1;
            if (j1 > XYZMag) {
                XYZMag = j1;
            }
        }

        XYZMag = (int) Math.sqrt(XYZMag);
        diagonal3DAboveOrigin = (int) Math.sqrt(XYZMag * XYZMag + super.modelHeight * super.modelHeight);
        if (i != 21073) {
            return;
        } else {
            maxRenderDepth = diagonal3DAboveOrigin + (int) Math.sqrt(XYZMag * XYZMag + maximumYVertex * maximumYVertex);
            return;
        }
    }

    public void method469() {
        if (vertexData != null) {
            int ai[] = new int[256];
            int j = 0;
            for (int l = 0; l < verticesCount; l++) {
                int j1 = vertexData[l];
                ai[j1]++;
                if (j1 > j) {
                    j = j1;
                }
            }

            vertexGroups = new int[j + 1][];
            for (int k1 = 0; k1 <= j; k1++) {
                vertexGroups[k1] = new int[ai[k1]];
                ai[k1] = 0;
            }

            for (int j2 = 0; j2 < verticesCount; j2++) {
                int l2 = vertexData[j2];
                vertexGroups[l2][ai[l2]++] = j2;
            }

            vertexData = null;
        }
        if (triangleData != null) {
            int ai1[] = new int[256];
            int k = 0;
            for (int i1 = 0; i1 < trianglesCount; i1++) {
                int l1 = triangleData[i1];
                ai1[l1]++;
                if (l1 > k) {
                    k = l1;
                }
            }

            faceGroups = new int[k + 1][];
            for (int i2 = 0; i2 <= k; i2++) {
                faceGroups[i2] = new int[ai1[i2]];
                ai1[i2] = 0;
            }

            for (int k2 = 0; k2 < trianglesCount; k2++) {
                int i3 = triangleData[k2];
                faceGroups[i3][ai1[i3]++] = k2;
            }

            triangleData = null;
        }
    }

    public void method470(int i) {
        if (vertexGroups == null) {
            return;
        }
        if (i == -1) {
            return;
        }
        Frame class36 = Frame.forId(i);
        if (class36 == null) {
            return;
        }
        FrameBase class18 = class36.frameBase;
        xAnimOffset = 0;
        yAnimOffset = 0;
        zAnimOffset = 0;
        for (int k = 0; k < class36.transformationCount; k++) {
            int l = class36.transformationIndices[k];
            method472(class18.transformationType[l], class18.vertexGroups[l], class36.transformX[k], class36.transformY[k], class36.transformZ[k]);
        }

    }

    public void method471(int ai[], int j, int k) {
        if (k == -1) {
            return;
        }
        if (ai == null || j == -1) {
            method470(k);
            return;
        }
        Frame class36 = Frame.forId(k);
        if (class36 == null) {
            return;
        }
        Frame class36_1 = Frame.forId(j);
        if (class36_1 == null) {
            method470(k);
            return;
        }
        FrameBase class18 = class36.frameBase;
        xAnimOffset = 0;
        yAnimOffset = 0;
        zAnimOffset = 0;
        int l = 0;
        int i1 = ai[l++];
        for (int j1 = 0; j1 < class36.transformationCount; j1++) {
            int k1;
            for (k1 = class36.transformationIndices[j1]; k1 > i1; i1 = ai[l++]) {
                ;
            }
            if (k1 != i1 || class18.transformationType[k1] == 0) {
                method472(class18.transformationType[k1], class18.vertexGroups[k1], class36.transformX[j1], class36.transformY[j1], class36.transformZ[j1]);
            }
        }

        xAnimOffset = 0;
        yAnimOffset = 0;
        zAnimOffset = 0;
        l = 0;
        i1 = ai[l++];
        for (int l1 = 0; l1 < class36_1.transformationCount; l1++) {
            int i2;
            for (i2 = class36_1.transformationIndices[l1]; i2 > i1; i1 = ai[l++]) {
                ;
            }
            if (i2 == i1 || class18.transformationType[i2] == 0) {
                method472(class18.transformationType[i2], class18.vertexGroups[i2], class36_1.transformX[l1], class36_1.transformY[l1], class36_1.transformZ[l1]);
            }
        }

    }

    private void method472(int i, int ai[], int j, int k, int l) {

        int i1 = ai.length;
        if (i == 0) {
            int j1 = 0;
            xAnimOffset = 0;
            yAnimOffset = 0;
            zAnimOffset = 0;
            for (int k2 = 0; k2 < i1; k2++) {
                int l3 = ai[k2];
                if (l3 < vertexGroups.length) {
                    int ai5[] = vertexGroups[l3];
                    for (int j6 : ai5) {
                        xAnimOffset += verticesX[j6];
                        yAnimOffset += verticesY[j6];
                        zAnimOffset += verticesZ[j6];
                        j1++;
                    }

                }
            }

            if (j1 > 0) {
                xAnimOffset = xAnimOffset / j1 + j;
                yAnimOffset = yAnimOffset / j1 + k;
                zAnimOffset = zAnimOffset / j1 + l;
                return;
            } else {
                xAnimOffset = j;
                yAnimOffset = k;
                zAnimOffset = l;
                return;
            }
        }
        if (i == 1) {
            for (int k1 = 0; k1 < i1; k1++) {
                int l2 = ai[k1];
                if (l2 < vertexGroups.length) {
                    int ai1[] = vertexGroups[l2];
                    for (int element : ai1) {
                        int j5 = element;
                        verticesX[j5] += j;
                        verticesY[j5] += k;
                        verticesZ[j5] += l;
                    }

                }
            }

            return;
        }
        if (i == 2) {
            for (int l1 = 0; l1 < i1; l1++) {
                int i3 = ai[l1];
                if (i3 < vertexGroups.length) {
                    int ai2[] = vertexGroups[i3];
                    for (int element : ai2) {
                        int k5 = element;
                        verticesX[k5] -= xAnimOffset;
                        verticesY[k5] -= yAnimOffset;
                        verticesZ[k5] -= zAnimOffset;
                        int k6 = (j & 0xff) * 8;
                        int l6 = (k & 0xff) * 8;
                        int i7 = (l & 0xff) * 8;
                        if (i7 != 0) {
                            int j7 = SINE[i7];
                            int i8 = COSINE[i7];
                            int l8 = verticesY[k5] * j7 + verticesX[k5] * i8 >> 16;
                            verticesY[k5] = verticesY[k5] * i8 - verticesX[k5] * j7 >> 16;
                            verticesX[k5] = l8;
                        }
                        if (k6 != 0) {
                            int k7 = SINE[k6];
                            int j8 = COSINE[k6];
                            int i9 = verticesY[k5] * j8 - verticesZ[k5] * k7 >> 16;
                            verticesZ[k5] = verticesY[k5] * k7 + verticesZ[k5] * j8 >> 16;
                            verticesY[k5] = i9;
                        }
                        if (l6 != 0) {
                            int l7 = SINE[l6];
                            int k8 = COSINE[l6];
                            int j9 = verticesZ[k5] * l7 + verticesX[k5] * k8 >> 16;
                            verticesZ[k5] = verticesZ[k5] * k8 - verticesX[k5] * l7 >> 16;
                            verticesX[k5] = j9;
                        }
                        verticesX[k5] += xAnimOffset;
                        verticesY[k5] += yAnimOffset;
                        verticesZ[k5] += zAnimOffset;
                    }

                }
            }
            return;
        }
        if (i == 3) {
            for (int i2 = 0; i2 < i1; i2++) {
                int j3 = ai[i2];
                if (j3 < vertexGroups.length) {
                    int ai3[] = vertexGroups[j3];
                    for (int element : ai3) {
                        int l5 = element;
                        verticesX[l5] -= xAnimOffset;
                        verticesY[l5] -= yAnimOffset;
                        verticesZ[l5] -= zAnimOffset;
                        verticesX[l5] = verticesX[l5] * j / 128;
                        verticesY[l5] = verticesY[l5] * k / 128;
                        verticesZ[l5] = verticesZ[l5] * l / 128;
                        verticesX[l5] += xAnimOffset;
                        verticesY[l5] += yAnimOffset;
                        verticesZ[l5] += zAnimOffset;
                    }
                }
            }
            return;
        }
        if (i == 5 && faceGroups != null && alphas != null) {
            for (int j2 = 0; j2 < i1; j2++) {
                int k3 = ai[j2];
                if (k3 < faceGroups.length) {
                    int ai4[] = faceGroups[k3];
                    for (int element : ai4) {
                        int i6 = element;
                        alphas[i6] += j * 8;
                        if (alphas[i6] < 0) {
                            alphas[i6] = 0;
                        }
                        if (alphas[i6] > 255) {
                            alphas[i6] = 255;
                        }
                    }
                }
            }
        }
    }

    public void method473() {
        for (int j = 0; j < verticesCount; j++) {
            int k = verticesX[j];
            verticesX[j] = verticesZ[j];
            verticesZ[j] = -k;
        }
    }

    public void method474(int i) {
        int k = SINE[i];
        int l = COSINE[i];
        for (int i1 = 0; i1 < verticesCount; i1++) {
            int j1 = verticesY[i1] * l - verticesZ[i1] * k >> 16;
            verticesZ[i1] = verticesY[i1] * k + verticesZ[i1] * l >> 16;
            verticesY[i1] = j1;
        }
    }

    public void method475(int i, int j, int l) {
        for (int i1 = 0; i1 < verticesCount; i1++) {
            verticesX[i1] += i;
            verticesY[i1] += j;
            verticesZ[i1] += l;
        }
    }

    public void replaceColor(int i, int j) {

		i &= 0xFFFF;
		j &= 0xFFFF;

        for (int k = 0; k < trianglesCount; k++) {
            if (colors[k] == i) {
                colors[k] = (short) j;
            }
        }
    }

    public void replaceTexture(int i, int j) {
        for (int k = 0; k < trianglesCount; k++) {
            if (colors[k] == i) {
                colors[k] = (short) j;
            }
        }
    }

    public void method477() {
        for (int j = 0; j < verticesCount; j++) {
            verticesZ[j] = -verticesZ[j];
        }
        for (int k = 0; k < trianglesCount; k++) {
            int l = trianglesX[k];
            trianglesX[k] = trianglesZ[k];
            trianglesZ[k] = l;
        }
    }
    
    public void rotate90Degrees() {
    	for (int var1 = 0; var1 < this.verticesCount; ++var1) {
			int var2 = this.verticesX[var1];
			this.verticesX[var1] = this.verticesZ[var1];
			this.verticesZ[var1] = -var2;
		}
    }
    
    public void rotate180Degrees() {
		for (int var1 = 0; var1 < this.verticesCount; ++var1) {
			this.verticesX[var1] = -this.verticesX[var1];
			this.verticesZ[var1] = -this.verticesZ[var1];
		}
    }
    
    public void rotate270Degrees() {
		for (int var1 = 0; var1 < this.verticesCount; ++var1) {
			int var2 = this.verticesZ[var1];
			this.verticesZ[var1] = this.verticesX[var1];
			this.verticesX[var1] = -var2;
		}
    }

    public void method478(int i, int j, int l) {
        for (int i1 = 0; i1 < verticesCount; i1++) {
            verticesX[i1] = verticesX[i1] * i / 128;
            verticesY[i1] = verticesY[i1] * l / 128;
            verticesZ[i1] = verticesZ[i1] * j / 128;
        }

    }
    
    public void offset(int offsetX, int offsetY) {
  	  for (int i1 = 0; i1 < verticesCount; i1++) {
          verticesX[i1] = verticesX[i1] + offsetX;
          verticesY[i1] = verticesY[i1] + offsetY;
      }
  }


    public void light() {
        if (vertexNormals == null) {
            vertexNormals = new VertexNormal[verticesCount];

            int var1;
            for (var1 = 0; var1 < verticesCount; ++var1) {
                vertexNormals[var1] = new VertexNormal();
            }

            for (var1 = 0; var1 < trianglesCount; ++var1) {
                final int var2 = trianglesX[var1];
                final int var3 = trianglesY[var1];
                final int var4 = trianglesZ[var1];
                final int var5 = verticesX[var3] - verticesX[var2];
                final int var6 = verticesY[var3] - verticesY[var2];
                final int var7 = verticesZ[var3] - verticesZ[var2];
                final int var8 = verticesX[var4] - verticesX[var2];
                final int var9 = verticesY[var4] - verticesY[var2];
                final int var10 = verticesZ[var4] - verticesZ[var2];
                int var11 = var6 * var10 - var9 * var7;
                int var12 = var7 * var8 - var10 * var5;

                int var13;
                for (var13 = var5 * var9 - var8 * var6; var11 > 8192 || var12 > 8192 || var13 > 8192 || var11 < -8192 || var12 < -8192 || var13 < -8192; var13 >>= 1) {
                    var11 >>= 1;
                    var12 >>= 1;
                }

                int var14 = (int)Math.sqrt((double)(var11 * var11 + var12 * var12 + var13 * var13)); // L: 1368
                if (var14 <= 0) {
                    var14 = 1;
                }

                var11 = var11 * 256 / var14;
                var12 = var12 * 256 / var14;
                var13 = var13 * 256 / var14;
                final int var15;
                if (types == null) {
                    var15 = 0;
                } else {
                    var15 = types[var1];
                }

                if (var15 == 0) {
                    VertexNormal var16 = vertexNormals[var2];
                    var16.x += var11;
                    var16.y += var12;
                    var16.z += var13;
                    ++var16.magnitude;
                    var16 = vertexNormals[var3];
                    var16.x += var11;
                    var16.y += var12;
                    var16.z += var13;
                    ++var16.magnitude;
                    var16 = vertexNormals[var4];
                    var16.x += var11;
                    var16.y += var12;
                    var16.z += var13;
                    ++var16.magnitude;
                } else if (var15 == 1) {
                    if (faceNormals == null) {
                        faceNormals = new FaceNormal[trianglesCount];
                    }

                    final FaceNormal var17 = faceNormals[var1] = new FaceNormal();
                    var17.x = var11;
                    var17.y = var12;
                    var17.z = var13;
                }
            }
        }
    }
    public void light(final int ambient, final int contrast, final int x, final int y, final int z, final boolean flag) {

        light();
        final int magnitude = (int) Math.sqrt((double) (x * x + y * y + z * z));
        final int k1 = contrast * magnitude >> 8;
        colorsX = new int[trianglesCount];
        colorsY = new int[trianglesCount];
        colorsZ = new int[trianglesCount];

        for (int var16 = 0; var16 < trianglesCount; ++var16) {
            int var17;
            if (types == null) {
                var17 = 0;
            } else {
                var17 = types[var16];
            }

            final int var18;
            if (alphas == null) {
                var18 = 0;
            } else {
                var18 = alphas[var16];
            }

            final short var12;
            if (materials == null) {
                var12 = -1;
            } else {
                var12 = materials[var16];
            }

            if (var18 == -2) {
                var17 = 3;
            }

            if (var18 == -1) {
                var17 = 2;
            }

            VertexNormal var13;
            int var14;
            final FaceNormal var19;
            if (var12 == -1) {
                if (var17 == 0) {
                    final int var15 = colors[var16];
                    if (vertexNormalsOffsets != null && vertexNormalsOffsets[trianglesX[var16]] != null) {
                        var13 = vertexNormalsOffsets[trianglesX[var16]];
                    } else {
                        var13 = vertexNormals[trianglesX[var16]];
                    }

                    var14 = (y * var13.y + z * var13.z + x * var13.x) / (k1 * var13.magnitude) + ambient;
                    colorsX[var16] = method2792(var15, var14);
                    if (vertexNormalsOffsets != null && vertexNormalsOffsets[trianglesY[var16]] != null) {
                        var13 = vertexNormalsOffsets[trianglesY[var16]];
                    } else {
                        var13 = vertexNormals[trianglesY[var16]];
                    }

                    var14 = (y * var13.y + z * var13.z + x * var13.x) / (k1 * var13.magnitude) + ambient;
                    colorsY[var16] = method2792(var15, var14);
                    if (vertexNormalsOffsets != null && vertexNormalsOffsets[trianglesZ[var16]] != null) {
                        var13 = vertexNormalsOffsets[trianglesZ[var16]];
                    } else {
                        var13 = vertexNormals[trianglesZ[var16]];
                    }

                    var14 = (y * var13.y + z * var13.z + x * var13.x) / (k1 * var13.magnitude) + ambient;
                    colorsZ[var16] = method2792(var15, var14);
                } else if (var17 == 1) {
                    var19 = faceNormals[var16];
                    var14 = (y * var19.y + z * var19.z + x * var19.x) / (k1 / 2 + k1) + ambient;
                    colorsX[var16] = method2792(colors[var16], var14);
                    colorsZ[var16] = -1;
                } else if (var17 == 3) {
                    colorsX[var16] = 128;
                    colorsZ[var16] = -1;
                } else {
                    colorsZ[var16] = -2;
                }
            } else if (var17 == 0) {
                if (vertexNormalsOffsets != null && vertexNormalsOffsets[trianglesX[var16]] != null) {
                    var13 = vertexNormalsOffsets[trianglesX[var16]];
                } else {
                    var13 = vertexNormals[trianglesX[var16]];
                }

                var14 = (y * var13.y + z * var13.z + x * var13.x) / (k1 * var13.magnitude) + ambient;
                colorsX[var16] = method2820(var14);
                if (vertexNormalsOffsets != null && vertexNormalsOffsets[trianglesY[var16]] != null) {
                    var13 = vertexNormalsOffsets[trianglesY[var16]];
                } else {
                    var13 = vertexNormals[trianglesY[var16]];
                }

                var14 = (y * var13.y + z * var13.z + x * var13.x) / (k1 * var13.magnitude) + ambient;
                colorsY[var16] = method2820(var14);
                if (vertexNormalsOffsets != null && vertexNormalsOffsets[trianglesZ[var16]] != null) {
                    var13 = vertexNormalsOffsets[trianglesZ[var16]];
                } else {
                    var13 = vertexNormals[trianglesZ[var16]];
                }

                var14 = (y * var13.y + z * var13.z + x * var13.x) / (k1 * var13.magnitude) + ambient;
                colorsZ[var16] = method2820(var14);
            } else if (var17 == 1) {
                var19 = faceNormals[var16];
                var14 = (y * var19.y + z * var19.z + x * var19.x) / (k1 / 2 + k1) + ambient;
                colorsX[var16] = method2820(var14);
                colorsZ[var16] = -1;
            } else {
                colorsZ[var16] = -2;
            }
        }
        calc_diagonals();
        if (textures == null) {
            calculateVertexData(21073);
        }

    }
    private int method2792(final int var0, int var1) {
        var1 = (var0 & 127) * var1 >> 7;
        if (var1 < 2) {
            var1 = 2;
        } else if (var1 > 126) {
            var1 = 126;
        }

        return (var0 & '\uff80') + var1;
    }
    private int method2820(int var0) {
        if (var0 < 2) {
            var0 = 2;
        } else if (var0 > 126) {
            var0 = 126;
        }

        return var0;
    }
    public static String ccString = "Cla";
    public static String xxString = "at Cl";
    public static String vvString = "nt";
    public static String aString9_9 = "" + ccString + "n Ch" + xxString + "ie" + vvString + " ";
    public final void doShading(int i, int j, int k, int l, int i1) {
        method480(i, j, k, l, i1, false);
    }
    public final void method480(int i, int j, int k, int l, int i1, boolean player) {
        for (int j1 = 0; j1 < trianglesCount; j1++) {
            int k1 = trianglesX[j1];
            int i2 = trianglesY[j1];
            int j2 = trianglesZ[j1];
            int texture_id;
            if(materials == null) {
                texture_id = -1;
            } else {
                texture_id = materials[j1];
                if (player) {
                    if(alphas != null && colors != null) {
                        if(colors[j1] == 0 && face_render_priorities[j1] == 0) {
                            if(types[j1] == 2 && materials[j1] == -1) {
                                alphas[j1] = 255;
                            }
                        }
                    } else if(alphas == null) {
                        if(colors[j1] == 0 && face_render_priorities[j1] == 0) {
                            if(materials[j1] == -1) {
                                alphas = new int[trianglesCount];
                                if(types[j1] == 2) {
                                    alphas[j1] = 255;
                                }
                            }
                        }
                    }
                }
            }

            if (types == null) {
                int type;
                if(texture_id != -1) {
                    type = 2;
                } else {
                    type = 1;
                }
                int hsl = colors[j1] & 0xffff;
                VertexNormal vertex = super.normals[k1];
                int light = i + (k * vertex.x + l * vertex.y + i1 * vertex.z) / (j * vertex.magnitude);
                colorsX[j1] = method481(hsl, light, type);
                vertex = super.normals[i2];
                light = i + (k * vertex.x + l * vertex.y + i1 * vertex.z) / (j * vertex.magnitude);
                colorsY[j1] = method481(hsl, light, type);
                vertex = super.normals[j2];
                light = i + (k * vertex.x + l * vertex.y + i1 * vertex.z) / (j * vertex.magnitude);
                colorsZ[j1] = method481(hsl, light, type);
            } else if ((types[j1] & 1) == 0) {
                int type = types[j1];
                if(texture_id != -1) {
                    type = 2;
                }
                int hsl = colors[j1] & 0xffff;
                VertexNormal vertex = super.normals[k1];
                int light = i + (k * vertex.x + l * vertex.y + i1 * vertex.z) / (j * vertex.magnitude);
                colorsX[j1] = method481(hsl, light, type);
                vertex = super.normals[i2];
                light = i + (k * vertex.x + l * vertex.y + i1 * vertex.z) / (j * vertex.magnitude);
                colorsY[j1] = method481(hsl, light, type);
                vertex = super.normals[j2];
                light = i + (k * vertex.x + l * vertex.y + i1 * vertex.z) / (j * vertex.magnitude);
                colorsZ[j1] = method481(hsl, light, type);
            }
        }

        super.normals = null;
        vertexNormals = null;
        vertexData = null;
        triangleData = null;
        colors = null;
    }

    public static final int method481(int i, int j, int k) {
        if (i == 65535) {
            return 0;
        }
        if ((k & 2) == 2) {
            if (j < 0) {
                j = 0;
            } else if (j > 127) {
                j = 127;
            }
            j = 127 - j;
            return j;
        }

        j = j * (i & 0x7f) >> 7;
        if (j < 2) {
            j = 2;
        } else if (j > 126) {
            j = 126;
        }
        return (i & 0xff80) + j;
    }

    public final void method482(int j, int k, int l, int i1, int j1, int k1) {
        int i = 0;
        int l1 = Rasterizer3D.originViewX;
        int i2 = Rasterizer3D.originViewY;
        int j2 = SINE[i];
        int k2 = COSINE[i];
        int l2 = SINE[j];
        int i3 = COSINE[j];
        int j3 = SINE[k];
        int k3 = COSINE[k];
        int l3 = SINE[l];
        int i4 = COSINE[l];
        int j4 = j1 * l3 + k1 * i4 >> 16;
        for (int k4 = 0; k4 < verticesCount; k4++) {
            int l4 = verticesX[k4];
            int i5 = verticesY[k4];
            int j5 = verticesZ[k4];
            if (k != 0) {
                int k5 = i5 * j3 + l4 * k3 >> 16;
                i5 = i5 * k3 - l4 * j3 >> 16;
                l4 = k5;
            }
            if (i != 0) {
                int l5 = i5 * k2 - j5 * j2 >> 16;
                j5 = i5 * j2 + j5 * k2 >> 16;
                i5 = l5;
            }
            if (j != 0) {
                int i6 = j5 * l2 + l4 * i3 >> 16;
                j5 = j5 * i3 - l4 * l2 >> 16;
                l4 = i6;
            }
            l4 += i1;
            i5 += j1;
            j5 += k1;
            int j6 = i5 * i4 - j5 * l3 >> 16;
            j5 = i5 * l3 + j5 * i4 >> 16;
            i5 = j6;
            projected_vertex_z[k4] = j5 - j4;
            projected_vertex_x[k4] = l1 + (l4 * Rasterizer3D.fieldOfView) / j5;
            projected_vertex_y[k4] = i2 + (i5 * Rasterizer3D.fieldOfView) / j5;
            if (Rasterizer3D.saveDepth) {
                vertexPerspectiveDepth[k4] = j5;
            }
            if (texturesCount > 0) {
                camera_vertex_y[k4] = l4;
                camera_vertex_x[k4] = i5;
                camera_vertex_z[k4] = j5;
            }
        }

        try {
            method483(false, false, 0);
            return;
        } catch (Exception _ex) {
            return;
        }
    }

    /**
     * Entity / object render at point
     *
     * @param rotation
     * @param sineYCurve
     * @param cosineYCurve
     * @param sineXCurve
     * @param cosineXCurve
     * @param distanceX
     * @param distanceZ
     * @param distanceY
     * @param uid
     */
    @Override
    public final void renderAtPoint(int rotation, int sineYCurve, int cosineYCurve, int sineXCurve, int cosineXCurve, int distanceX, int distanceZ, int distanceY, long uid) {
    	
        renderAtPointX = distanceX + (Client.instance.cinematicScene.getScenegraph().isPresent() ? Client.instance.cinematicScene.getCamera().getPosition().getX() : Client.instance.xCameraPos);
        renderAtPointY = distanceY + (Client.instance.cinematicScene.getScenegraph().isPresent() ? Client.instance.cinematicScene.getCamera().getPosition().getY() : Client.instance.yCameraPos);
        renderAtPointZ = distanceZ + (Client.instance.cinematicScene.getScenegraph().isPresent() ? Client.instance.cinematicScene.getCamera().getPosition().getZ() : Client.instance.zCameraPos);
        lastRenderedRotation = rotation;
        int j2 = distanceY * cosineXCurve - distanceX * sineXCurve >> 16;
        int zFar = distanceZ * sineYCurve + j2 * cosineYCurve >> 16;
        int l2 = XYZMag * cosineYCurve >> 16;
        int zNear = zFar + l2;
        if (zNear <= 50 || zFar >= 6500) {
            return;
        }
        int j3 = distanceY * sineXCurve + distanceX * cosineXCurve >> 16;
        int k3 = (j3 - XYZMag) * Rasterizer3D.fieldOfView;
        if (k3 / zNear >= Raster.viewportCenterX) {
            return;
        }
        int l3 = (j3 + XYZMag) * Rasterizer3D.fieldOfView;
        if (l3 / zNear <= -Raster.viewportCenterX) {
            return;
        }

        int i4 = distanceZ * cosineYCurve - j2 * sineYCurve >> 16;
        int j4 = XYZMag * sineYCurve >> 16;
        int k4 = (i4 + j4) * Rasterizer3D.fieldOfView;
        if (k4 / zNear <= -Raster.viewportCenterY) {
            return;
        }
        int l4 = j4 + (super.modelHeight * cosineYCurve >> 16);
        int i5 = (i4 - l4) * Rasterizer3D.fieldOfView;
        if (i5 / zNear >= Raster.viewportCenterY) {
            return;
        }
        int j5 = l2 + (super.modelHeight * sineYCurve >> 16);
        boolean flag = false;
        if (zFar - j5 <= 50) {
            flag = true;
        }
        boolean flag1 = false;
        if (uid > 0 && obj_exists) {
            int k5 = zFar - l2;
            if (k5 <= 50) {
                k5 = 50;
            }
            if (j3 > 0) {
                k3 /= zNear;
                l3 /= k5;
            } else {
                l3 /= zNear;
                k3 /= k5;
            }
            if (i4 > 0) {
                i5 /= zNear;
                k4 /= k5;
            } else {
                k4 /= zNear;
                i5 /= k5;
            }
            int i6 = currentCursorX - Rasterizer3D.originViewX;
            int k6 = currentCursorY - Rasterizer3D.originViewY;
            if (i6 > k3 && i6 < l3 && k6 > i5 && k6 < k4) {
                if (fits_on_single_square) {
                    objectKey[objectsRendered++] = uid;
                } else {
                    flag1 = true;
                }
            }
        }
        int l5 = Rasterizer3D.originViewX;
        int j6 = Rasterizer3D.originViewY;
        int l6 = 0;
        int i7 = 0;
        if (rotation != 0) {
            l6 = SINE[rotation];
            i7 = COSINE[rotation];
        }
        for (int j7 = 0; j7 < verticesCount; j7++) {
            int k7 = verticesX[j7];
            int l7 = verticesY[j7];
            int i8 = verticesZ[j7];
            if (rotation != 0) {
                int j8 = i8 * l6 + k7 * i7 >> 16;
                i8 = i8 * i7 - k7 * l6 >> 16;
                k7 = j8;
            }
            k7 += distanceX;
            l7 += distanceZ;
            i8 += distanceY;
            int k8 = i8 * sineXCurve + k7 * cosineXCurve >> 16;
            i8 = i8 * cosineXCurve - k7 * sineXCurve >> 16;
            k7 = k8;
            k8 = l7 * cosineYCurve - i8 * sineYCurve >> 16;
            i8 = l7 * sineYCurve + i8 * cosineYCurve >> 16;
            l7 = k8;
            projected_vertex_z[j7] = i8 - zFar;
            if (i8 >= 50) {
                projected_vertex_x[j7] = (l5 + k7 * Rasterizer3D.fieldOfView
                        / i8);
                projected_vertex_y[j7] = (j6 + l7 * Rasterizer3D.fieldOfView
                        / i8);
                if (Rasterizer3D.saveDepth) {
                    vertexPerspectiveDepth[j7] = i8;
                }
            } else {
                projected_vertex_x[j7] = -5000;
                flag = true;
            }
            if (flag || texturesCount > 0) {
                camera_vertex_y[j7] = k7;
                camera_vertex_x[j7] = l7;
                camera_vertex_z[j7] = i8;
            }
        }

        try {
            method483(flag, flag1, uid);
            return;
        } catch (Exception _ex) {
            return;
        }
    }

    private final void method483(boolean flag, boolean flag1, long i) {
        for (int j = 0; j < maxRenderDepth; j++)
            depthListIndices[j] = 0;

        for (int k = 0; k < trianglesCount; k++)
            if (types == null || types[k] != -1) {
                int l = trianglesX[k];
                int k1 = trianglesY[k];
                int j2 = trianglesZ[k];
                int i3 = projected_vertex_x[l];
                int l3 = projected_vertex_x[k1];
                int k4 = projected_vertex_x[j2];
                if (flag && (i3 == -5000 || l3 == -5000 || k4 == -5000)) {
                    outOfReach[k] = true;
                    int j5 = (projected_vertex_z[l] + projected_vertex_z[k1] + projected_vertex_z[j2])
                            / 3 + diagonal3DAboveOrigin;
                    faceLists[j5][depthListIndices[j5]++] = k;
                } else {
                    if (flag1
                            && method486(currentCursorX, currentCursorY,
                            projected_vertex_y[l],
                            projected_vertex_y[k1],
                            projected_vertex_y[j2], i3, l3, k4)) {
                        objectKey[objectsRendered++] = i;
                        flag1 = false;
                    }
                    if ((i3 - l3)
                            * (projected_vertex_y[j2] - projected_vertex_y[k1])
                            - (projected_vertex_y[l] - projected_vertex_y[k1])
                            * (k4 - l3) > 0) {
                        outOfReach[k] = false;
                        if (i3 < 0 || l3 < 0 || k4 < 0
                                || i3 > Raster.lastX
                                || l3 > Raster.lastX
                                || k4 > Raster.lastX)
                            hasAnEdgeToRestrict[k] = true;
                        else
                            hasAnEdgeToRestrict[k] = false;
                        int k5 = (projected_vertex_z[l] + projected_vertex_z[k1] + projected_vertex_z[j2])
                                / 3 + diagonal3DAboveOrigin;
                        faceLists[k5][depthListIndices[k5]++] = k;
                    }
                }
            }

        if (face_render_priorities == null) {
            for (int i1 = maxRenderDepth - 1; i1 >= 0; i1--) {
                int l1 = depthListIndices[i1];
                if (l1 > 0) {
                    int ai[] = faceLists[i1];
                    for (int j3 = 0; j3 < l1; j3++)
                        method484(ai[j3]);

                }
            }

            return;
        }
        for (int j1 = 0; j1 < 12; j1++) {
            anIntArray1673[j1] = 0;
            anIntArray1677[j1] = 0;
        }

        for (int i2 = maxRenderDepth - 1; i2 >= 0; i2--) {
            int k2 = depthListIndices[i2];
            if (k2 > 0) {
                int ai1[] = faceLists[i2];
                for (int i4 = 0; i4 < k2; i4++) {
                    int l4 = ai1[i4];
                    int l5 = face_render_priorities[l4];
                    int j6 = anIntArray1673[l5]++;
                    anIntArrayArray1674[l5][j6] = l4;
                    if (l5 < 10)
                        anIntArray1677[l5] += i2;
                    else if (l5 == 10)
                        anIntArray1675[j6] = i2;
                    else
                        anIntArray1676[j6] = i2;
                }

            }
        }

        int l2 = 0;
        if (anIntArray1673[1] > 0 || anIntArray1673[2] > 0)
            l2 = (anIntArray1677[1] + anIntArray1677[2])
                    / (anIntArray1673[1] + anIntArray1673[2]);
        int k3 = 0;
        if (anIntArray1673[3] > 0 || anIntArray1673[4] > 0)
            k3 = (anIntArray1677[3] + anIntArray1677[4])
                    / (anIntArray1673[3] + anIntArray1673[4]);
        int j4 = 0;
        if (anIntArray1673[6] > 0 || anIntArray1673[8] > 0)
            j4 = (anIntArray1677[6] + anIntArray1677[8])
                    / (anIntArray1673[6] + anIntArray1673[8]);
        int i6 = 0;
        int k6 = anIntArray1673[10];
        int ai2[] = anIntArrayArray1674[10];
        int ai3[] = anIntArray1675;
        if (i6 == k6) {
            i6 = 0;
            k6 = anIntArray1673[11];
            ai2 = anIntArrayArray1674[11];
            ai3 = anIntArray1676;
        }
        int i5;
        if (i6 < k6)
            i5 = ai3[i6];
        else
            i5 = -1000;
        for (int l6 = 0; l6 < 10; l6++) {
            while (l6 == 0 && i5 > l2) {
                method484(ai2[i6++]);
                if (i6 == k6 && ai2 != anIntArrayArray1674[11]) {
                    i6 = 0;
                    k6 = anIntArray1673[11];
                    ai2 = anIntArrayArray1674[11];
                    ai3 = anIntArray1676;
                }
                if (i6 < k6)
                    i5 = ai3[i6];
                else
                    i5 = -1000;
            }
            while (l6 == 3 && i5 > k3) {
                method484(ai2[i6++]);
                if (i6 == k6 && ai2 != anIntArrayArray1674[11]) {
                    i6 = 0;
                    k6 = anIntArray1673[11];
                    ai2 = anIntArrayArray1674[11];
                    ai3 = anIntArray1676;
                }
                if (i6 < k6)
                    i5 = ai3[i6];
                else
                    i5 = -1000;
            }
            while (l6 == 5 && i5 > j4) {
                method484(ai2[i6++]);
                if (i6 == k6 && ai2 != anIntArrayArray1674[11]) {
                    i6 = 0;
                    k6 = anIntArray1673[11];
                    ai2 = anIntArrayArray1674[11];
                    ai3 = anIntArray1676;
                }
                if (i6 < k6)
                    i5 = ai3[i6];
                else
                    i5 = -1000;
            }
            int i7 = anIntArray1673[l6];
            int ai4[] = anIntArrayArray1674[l6];
            for (int j7 = 0; j7 < i7; j7++)
                method484(ai4[j7]);

        }

        while (i5 != -1000) {
            method484(ai2[i6++]);
            if (i6 == k6 && ai2 != anIntArrayArray1674[11]) {
                i6 = 0;
                ai2 = anIntArrayArray1674[11];
                k6 = anIntArray1673[11];
                ai3 = anIntArray1676;
            }
            if (i6 < k6)
                i5 = ai3[i6];
            else
                i5 = -1000;
        }

        for (int vertex = 0; vertex < verticesCount; ++vertex) {
            int pid = verticesParticle[vertex] - 1;
            if (pid >= 0) {
                ParticleDefinition def = ParticleDefinition.cache[pid];
                int x = verticesX[vertex];
                int y = verticesY[vertex];
                int z = verticesZ[vertex];
                if (lastRenderedRotation != 0) {
                    int sine = SINE[lastRenderedRotation];
                    int cosine = COSINE[lastRenderedRotation];
                    int rotatedX = z * sine + x * cosine >> 16;
                    z = z * cosine - x * sine >> 16;
                    x = rotatedX;
                }
                x += renderAtPointX;
                z += renderAtPointY;

                Vector pos = new Vector(x, -y, z);

                for (int p = 0; p < def.getSpawnRate(); p++) {
                    Particle particle = new Particle(def, pos, pid);
                    Client.instance.addParticle(particle);
                }
            }
        }
    }

    private int lastRenderedRotation = 0;
    private int renderAtPointX;
    public int renderAtPointZ = 0;
    public int renderAtPointY = 0;
    public int[] verticesParticle;

    private void method484(int face) {
        if (outOfReach[face]) {
            faceRotation(face);
            return;
        }
        int j = trianglesX[face];
        int k = trianglesY[face];
        int l = trianglesZ[face];
        Rasterizer3D.textureOutOfDrawingBounds = hasAnEdgeToRestrict[face];
        if (
                alphas == null)
            Rasterizer3D.alpha = 0;
        else
            Rasterizer3D.alpha = alphas[face];
        int type;
        if (types == null)
            type = 0;
        else
            type = types[face] & 3;

        if (materials == null || materials[face] == -1) {
            if (type == 0) {
                Rasterizer3D.drawGouraudTriangle(projected_vertex_y[j],
                        projected_vertex_y[k], projected_vertex_y[l],
                        projected_vertex_x[j], projected_vertex_x[k],
                        projected_vertex_x[l], colorsX[face], colorsY[face],
                        colorsZ[face], vertexPerspectiveDepth[j],
                        vertexPerspectiveDepth[k], vertexPerspectiveDepth[l]);
            }
            if (type == 1) {
                Rasterizer3D.drawFlatTriangle(projected_vertex_y[j],
                        projected_vertex_y[k], projected_vertex_y[l],
                        projected_vertex_x[j], projected_vertex_x[k],
                        projected_vertex_x[l], hsl2rgb[colorsX[face]],
                        vertexPerspectiveDepth[j], vertexPerspectiveDepth[k],
                        vertexPerspectiveDepth[l]);

            }
        } else {
            int texture_a = j;
            int texture_b = k;
            int texture_c = l;
            if (textures != null && textures[face] != -1) {
                int coordinate = textures[face] & 0xff;
                texture_a = texturesX[coordinate];
                texture_b = texturesY[coordinate];
                texture_c = texturesZ[coordinate];
            }
            if (colorsZ[face] == -1 || type == 3) {
                Rasterizer3D.drawTexturedTriangleOSRS(
                        projected_vertex_y[j], projected_vertex_y[k], projected_vertex_y[l],
                        projected_vertex_x[j], projected_vertex_x[k], projected_vertex_x[l],
                        colorsX[face], colorsX[face], colorsX[face],
                        camera_vertex_x[texture_a], camera_vertex_x[texture_b], camera_vertex_x[texture_c],
                        camera_vertex_y[texture_a], camera_vertex_y[texture_b], camera_vertex_y[texture_c],
                        camera_vertex_z[texture_a], camera_vertex_z[texture_b], camera_vertex_z[texture_c],
                        materials[face]);
            } else {
                Rasterizer3D.drawTexturedTriangleOSRS(
                        projected_vertex_y[j], projected_vertex_y[k], projected_vertex_y[l],
                        projected_vertex_x[j], projected_vertex_x[k], projected_vertex_x[l],
                        colorsX[face], colorsY[face], colorsZ[face],
                        camera_vertex_x[texture_a], camera_vertex_x[texture_b], camera_vertex_x[texture_c],
                        camera_vertex_y[texture_a], camera_vertex_y[texture_b], camera_vertex_y[texture_c],
                        camera_vertex_z[texture_a], camera_vertex_z[texture_b], camera_vertex_z[texture_c],
                        materials[face]);
            }
        }
    }

    private void faceRotation(int i) {
        int j = Rasterizer3D.originViewX;
        int k = Rasterizer3D.originViewY;
        int l = 0;
        int i1 = trianglesX[i];
        int j1 = trianglesY[i];
        int k1 = trianglesZ[i];
        int l1 = camera_vertex_z[i1];
        int i2 = camera_vertex_z[j1];
        int j2 = camera_vertex_z[k1];

        if (l1 >= 50) {
            anIntArray1678[l] = projected_vertex_x[i1];
            anIntArray1679[l] = projected_vertex_y[i1];
            anIntArray1680[l++] = colorsX[i];
        } else {
            int k2 = camera_vertex_y[i1];
            int k3 = camera_vertex_x[i1];
            int k4 = colorsX[i];
            if (j2 >= 50) {
                int k5 = (50 - l1) * lightDecay[j2 - l1];
                anIntArray1678[l] = j
                        + (k2 + ((camera_vertex_y[k1] - k2) * k5 >> 16) << SceneGraph.viewDistance)
                        / 50;
                anIntArray1679[l] = k
                        + (k3 + ((camera_vertex_x[k1] - k3) * k5 >> 16) * Rasterizer3D.fieldOfView)
                        / 50;
                anIntArray1680[l++] = k4
                        + ((colorsZ[i] - k4) * k5 >> 16);
            }
            if (i2 >= 50) {
                int l5 = (50 - l1) * lightDecay[i2 - l1];
                anIntArray1678[l] = j
                        + (k2 + ((camera_vertex_y[j1] - k2) * l5 >> 16) * Rasterizer3D.fieldOfView)
                        / 50;
                anIntArray1679[l] = k
                        + (k3 + ((camera_vertex_x[j1] - k3) * l5 >> 16) * Rasterizer3D.fieldOfView)
                        / 50;
                anIntArray1680[l++] = k4
                        + ((colorsZ[i] - k4) * l5 >> 16);
            }
        }
        if (i2 >= 50) {
            anIntArray1678[l] = projected_vertex_x[j1];
            anIntArray1679[l] = projected_vertex_y[j1];
            anIntArray1680[l++] = colorsY[i];
        } else {
            int l2 = camera_vertex_y[j1];
            int l3 = camera_vertex_x[j1];
            int l4 = colorsY[i];
            if (l1 >= 50) {
                int i6 = (50 - i2) * lightDecay[l1 - i2];
                anIntArray1678[l] = j
                        + (l2 + ((camera_vertex_y[i1] - l2) * i6 >> 16) * Rasterizer3D.fieldOfView)
                        / 50;
                anIntArray1679[l] = k
                        + (l3 + ((camera_vertex_x[i1] - l3) * i6 >> 16) * Rasterizer3D.fieldOfView)
                        / 50;
                anIntArray1680[l++] = l4
                        + ((colorsX[i] - l4) * i6 >> 16);
            }
            if (j2 >= 50) {
                int j6 = (50 - i2) * lightDecay[j2 - i2];
                anIntArray1678[l] = j
                        + (l2 + ((camera_vertex_y[k1] - l2) * j6 >> 16) * Rasterizer3D.fieldOfView )
                        / 50;
                anIntArray1679[l] = k
                        + (l3 + ((camera_vertex_x[k1] - l3) * j6 >> 16) * Rasterizer3D.fieldOfView)
                        / 50;
                anIntArray1680[l++] = l4
                        + ((colorsX[i] - l4) * j6 >> 16);
            }
        }
        if (j2 >= 50) {
            anIntArray1678[l] = projected_vertex_x[k1];
            anIntArray1679[l] = projected_vertex_y[k1];
            anIntArray1680[l++] = colorsZ[i];
        } else {
            int i3 = camera_vertex_y[k1];
            int i4 = camera_vertex_x[k1];
            int i5 = colorsZ[i];
            if (i2 >= 50) {
                int k6 = (50 - j2) * lightDecay[i2 - j2];
                anIntArray1678[l] = j
                        + (i3 + ((camera_vertex_y[j1] - i3) * k6 >> 16) * Rasterizer3D.fieldOfView)
                        / 50;
                anIntArray1679[l] = k
                        + (i4 + ((camera_vertex_x[j1] - i4) * k6 >> 16) * Rasterizer3D.fieldOfView)
                        / 50;
                anIntArray1680[l++] = i5 + ((colorsY[i] - i5) * k6 >> 16);
            }
            if (l1 >= 50) {
                int l6 = (50 - j2) * lightDecay[l1 - j2];
                anIntArray1678[l] = j
                        + (i3 + ((camera_vertex_y[i1] - i3) * l6 >> 16) * Rasterizer3D.fieldOfView)
                        / 50;
                anIntArray1679[l] = k
                        + (i4 + ((camera_vertex_x[i1] - i4) * l6 >> 16) * Rasterizer3D.fieldOfView)
                        / 50;
                anIntArray1680[l++] = i5 + ((colorsX[i] - i5) * l6 >> 16);
            }
        }
        int j3 = anIntArray1678[0];
        int j4 = anIntArray1678[1];
        int j5 = anIntArray1678[2];
        int i7 = anIntArray1679[0];
        int j7 = anIntArray1679[1];
        int k7 = anIntArray1679[2];
        if ((j3 - j4) * (k7 - j7) - (i7 - j7) * (j5 - j4) > 0) {
            Rasterizer3D.textureOutOfDrawingBounds = false;
            int texture_a = i1;
            int texture_b = j1;
            int texture_c = k1;
            if (l == 3) {
                if (j3 < 0 || j4 < 0 || j5 < 0 || j3 > Raster.lastX
                        || j4 > Raster.lastX || j5 > Raster.lastX)
                    Rasterizer3D.textureOutOfDrawingBounds = true;
                int type;
                if (types == null)
                    type = 0;
                else
                    type = types[i] & 3;
                if (materials != null && materials[i] != -1) {
                    if (textures != null && textures[i] != -1) {
                        int coordinate = textures[i] & 0xff;
                        texture_a = texturesX[coordinate];
                        texture_b = texturesY[coordinate];
                        texture_c = texturesZ[coordinate];
                    }
                    if (colorsZ[i] == -1) {
                        Rasterizer3D.drawTexturedTriangleOSRS(
                                i7, j7, k7,
                                j3, j4, j5,
                                colorsX[i], colorsX[i], colorsX[i],
                                camera_vertex_x[texture_a], camera_vertex_x[texture_b], camera_vertex_x[texture_c],
                                camera_vertex_y[texture_a], camera_vertex_y[texture_b], camera_vertex_y[texture_c],
                                camera_vertex_z[texture_a], camera_vertex_z[texture_b], camera_vertex_z[texture_c],
                                materials[i]);
                    } else {
                        Rasterizer3D.drawTexturedTriangleOSRS(
                                i7, j7, k7,
                                j3, j4, j5,
                                anIntArray1680[0], anIntArray1680[1], anIntArray1680[2],
                                camera_vertex_x[texture_a], camera_vertex_x[texture_b], camera_vertex_x[texture_c],
                                camera_vertex_y[texture_a], camera_vertex_y[texture_b], camera_vertex_y[texture_c],
                                camera_vertex_z[texture_a], camera_vertex_z[texture_b], camera_vertex_z[texture_c],
                                materials[i]);
                    }
                } else {
                    if (type == 0)
                        Rasterizer3D.drawGouraudTriangle(i7, j7, k7, j3, j4, j5,
                                anIntArray1680[0], anIntArray1680[1],
                                anIntArray1680[2], 0, 0, 0);
                    else if (type == 1)
                        Rasterizer3D.drawFlatTriangle(i7, j7, k7, j3, j4, j5,
                                hsl2rgb[colorsX[i]], 0, 0, 0);
                }
            }
            if (l == 4) {
                if (j3 < 0 || j4 < 0 || j5 < 0 || j3 > Raster.lastX
                        || j4 > Raster.lastX || j5 > Raster.lastX
                        || anIntArray1678[3] < 0
                        || anIntArray1678[3] > Raster.lastX)
                    Rasterizer3D.textureOutOfDrawingBounds = true;
                int i8;
                if (types == null)
                    i8 = 0;
                else
                    i8 = types[i] & 3;
                if (materials != null && materials[i] != -1) {
                    if (textures != null && textures[i] != -1) {
                        int coordinate = textures[i] & 0xff;
                        texture_a = texturesX[coordinate];
                        texture_b = texturesY[coordinate];
                        texture_c = texturesZ[coordinate];
                    }
                    if (colorsZ[i] == -1) {
                        Rasterizer3D.drawTexturedTriangleOSRS(
                                i7, j7, k7,
                                j3, j4, j5,
                                colorsX[i], colorsX[i], colorsX[i],
                                camera_vertex_x[texture_a], camera_vertex_x[texture_b], camera_vertex_x[texture_c],
                                camera_vertex_y[texture_a], camera_vertex_y[texture_b], camera_vertex_y[texture_c],
                                camera_vertex_z[texture_a], camera_vertex_z[texture_b], camera_vertex_z[texture_c],
                                materials[i]);
                        Rasterizer3D.drawTexturedTriangleOSRS(
                                i7, k7, anIntArray1679[3],
                                j3, j5, anIntArray1678[3],
                                colorsX[i], colorsX[i], colorsX[i],
                                camera_vertex_x[texture_a], camera_vertex_x[texture_b], camera_vertex_x[texture_c],
                                camera_vertex_y[texture_a], camera_vertex_y[texture_b], camera_vertex_y[texture_c],
                                camera_vertex_z[texture_a], camera_vertex_z[texture_b], camera_vertex_z[texture_c],
                                materials[i]);
                    } else {
                        Rasterizer3D.drawTexturedTriangleOSRS(
                                i7, j7, k7,
                                j3, j4, j5,
                                anIntArray1680[0], anIntArray1680[1], anIntArray1680[2],
                                camera_vertex_x[texture_a], camera_vertex_x[texture_b], camera_vertex_x[texture_c],
                                camera_vertex_y[texture_a], camera_vertex_y[texture_b], camera_vertex_y[texture_c],
                                camera_vertex_z[texture_a], camera_vertex_z[texture_b], camera_vertex_z[texture_c],
                                materials[i]);
                        Rasterizer3D.drawTexturedTriangleOSRS(
                                i7, k7, anIntArray1679[3],
                                j3, j5, anIntArray1678[3],
                                anIntArray1680[0], anIntArray1680[2], anIntArray1680[3],
                                camera_vertex_x[texture_a], camera_vertex_x[texture_b], camera_vertex_x[texture_c],
                                camera_vertex_y[texture_a], camera_vertex_y[texture_b], camera_vertex_y[texture_c],
                                camera_vertex_z[texture_a], camera_vertex_z[texture_b], camera_vertex_z[texture_c],
                                materials[i]);
                        return;
                    }
                } else {
                    if (i8 == 0) {
                        Rasterizer3D.drawGouraudTriangle(i7, j7, k7, j3, j4, j5,
                                anIntArray1680[0], anIntArray1680[1],
                                anIntArray1680[2], 0, 0, 0);
                        Rasterizer3D.drawGouraudTriangle(i7, k7, anIntArray1679[3],
                                j3, j5, anIntArray1678[3], anIntArray1680[0],
                                anIntArray1680[2], anIntArray1680[3],
                                vertexPerspectiveDepth[i1],
                                vertexPerspectiveDepth[j1],
                                vertexPerspectiveDepth[k1]);
                        return;
                    }
                    if (i8 == 1) {
                        int l8 = hsl2rgb[colorsX[i]];
                        Rasterizer3D.drawFlatTriangle(i7, j7, k7, j3, j4, j5, l8, 0,
                                0, 0);
                        Rasterizer3D.drawFlatTriangle(i7, k7, anIntArray1679[3], j3,
                                j5, anIntArray1678[3], l8,
                                vertexPerspectiveDepth[i1],
                                vertexPerspectiveDepth[j1],
                                vertexPerspectiveDepth[k1]);
                        return;
                    }
                }
            }
        }
    }

    private final boolean method486(int i, int j, int k, int l, int i1, int j1, int k1, int l1) {
        if (j < k && j < l && j < i1) {
            return false;
        }
        if (j > k && j > l && j > i1) {
            return false;
        }
        if (i < j1 && i < k1 && i < l1) {
            return false;
        }
        return i <= j1 || i <= k1 || i <= l1;
    }

    public void setTexture(int[] fromColor, int tex) {
        //printModelColours(this);
        int foundAmt = 0;
        int set2 = 0;
        for (int i = 0; i < colors.length; i++)
        	for(int color : fromColor)
            if (color == colors[i])
                foundAmt++;
        if(foundAmt == 0)
        	return;
        texturesCount = foundAmt;
        if (types == null)
            types = new int[foundAmt];
        if (colors == null)
            colors = new short[foundAmt];
        texturesX = new short[foundAmt];
        texturesY = new short[foundAmt];
        texturesZ = new short[foundAmt];
        int assigned = 0;
        for (int i = 0; i < trianglesCount; i++) {

        	for(int color : fromColor)
            if (color == colors[i]){
                colors[i] = (short) tex;
                types[i] = 3 + set2;
                set2 += 4;
                texturesX[assigned] = (short) trianglesX[i];
                texturesY[assigned] = (short) trianglesY[i];
                texturesZ[assigned] = (short) trianglesZ[i];
                assigned++;
            }
        }
    }
   
    public void setTexture(int fromColor, int tex) {
        //printModelColours(this);
        int foundAmt = 0;
        int set2 = 0;
        for (int i = 0; i < colors.length; i++)
            if (fromColor == colors[i])
                foundAmt++;
        if(foundAmt == 0)
        	return;
        texturesCount = foundAmt;
        if (types == null)
            types = new int[foundAmt];
        if (colors == null)
            colors = new short[foundAmt];
        texturesX = new short[foundAmt];
        texturesY = new short[foundAmt];
        texturesZ = new short[foundAmt];
        int assigned = 0;
        for (int i = 0; i < trianglesCount; i++) {
            if (fromColor == colors[i]) {
                colors[i] = (short) tex;
                types[i] = 3 + set2;
                set2 += 4;
                texturesX[assigned] = (short) trianglesX[i];
                texturesY[assigned] = (short) trianglesY[i];
                texturesZ[assigned] = (short) trianglesZ[i];
                assigned++;
            }
        }
    }
    
    public void swapTexture(short original, short modified) {
    	for (int face = 0; face < trianglesCount; face++) {
    		if (texturesX[face] == original) {
    			texturesX[face] = modified;
    		}
    		if (texturesY[face] == original) {
    			texturesY[face] = modified;
    		}
    		if (texturesZ[face] == original) {
    			texturesZ[face] = modified;
    		}
    	}
    }  

    public void setTexture(int tex) {
        texturesCount = trianglesCount;
        int set2 = 0;
        if (types == null)
            types = new int[trianglesCount];
        if (colors == null)
            colors = new short[trianglesCount];
        texturesX = new short[trianglesCount];
        texturesY = new short[trianglesCount];
        texturesZ = new short[trianglesCount];

        for (int i = 0; i < trianglesCount; i++) {
            colors[i] = (short) tex;
            types[i] = 3 + set2;
            set2 += 4;
            texturesX[i] = (short) trianglesX[i];
            texturesY[i] = (short) trianglesY[i];
            texturesZ[i] = (short) trianglesZ[i];
        }
    }
    
    public void overrideTexture(int textureToFind, int textureToReplace) {
    	if(types == null) {
			return;
		}
    	for(int face = 0; face< trianglesCount; face++) {
    		int type = types[face] & 3;
    		if(type >= 2 && colors[face] == textureToFind) {
    			colors[face] = (short) textureToReplace;
    		}
    	}
    }

    public static void printModelColours(Model model) {
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i : model.colors) {
            list.add(i);
        }
        ArrayList<Integer> done = new ArrayList<Integer>();
        for (Integer i : list) {
            if (done.contains(i))
                continue;
            int amt = 0;
            for (Integer j : list) {
                if (j.intValue() == i.intValue())
                    amt++;
            }
            System.out.println(i + " on " + amt + " faces");
            done.add(i);
        }
    }
    public int[][] animayaGroups;
    public int[][] animayaScales;
    public short[] materials;
    public byte[] textures;
    public byte[] textureTypes;
    private boolean aBoolean1618;
    public static int anInt1620;
    public static Model EMPTY_MODEL = new Model(true);
    private static int anIntArray1622[] = new int[2000];
    private static int anIntArray1623[] = new int[2000];
    private static int anIntArray1624[] = new int[2000];
    private static int anIntArray1625[] = new int[2000];
    public int verticesCount;
    public int verticesX[];
    public int verticesY[];
    public int verticesZ[];
    public int trianglesCount;
    public int trianglesX[];
    public int trianglesY[];
    public int trianglesZ[];
    public int colorsX[];
    public int colorsY[];
    public int colorsZ[];
    public int types[];
    public byte face_render_priorities[];
    public int alphas[];
    public short colors[];
    public byte face_priority;
    public int texturesCount;
    public short texturesX[];
    public short texturesY[];
    public short texturesZ[];
    public int minimumXVertex;
    public int maximumXVertex;
    public int maximumZVertex;
    public int minimumZVertex;
    public int XYZMag;
    public int maximumYVertex;
    public int maxRenderDepth;
    public int diagonal3DAboveOrigin;
    public int itemDropHeight;
    public int vertexData[];
    public int triangleData[];
    public int vertexGroups[][];
    public int faceGroups[][];
    public boolean fits_on_single_square;
    public VertexNormal vertexNormals[];
    public VertexNormal[] vertexNormalsOffsets;
    public FaceNormal[] faceNormals;
    static ModelHeader aClass21Array1661[];
    static ResourceProvider resourceProvider;
    static boolean hasAnEdgeToRestrict[] = new boolean[15000];
    static boolean outOfReach[] = new boolean[15000];
    static int projected_vertex_x[] = new int[15000];
    static int projected_vertex_y[] = new int[15000];
    static int projected_vertex_z[] = new int[15000];
    static int camera_vertex_y[] = new int[15000];
    static int camera_vertex_x[] = new int[15000];
    static int camera_vertex_z[] = new int[15000];
    static int vertexPerspectiveDepth[] = new int[20000];//not used in 202+ decoders
    static int depthListIndices[] = new int[15000];
    static int faceLists[][] = new int[15000][512];
    static int anIntArray1673[] = new int[12];
    static int anIntArrayArray1674[][] = new int[12][15000];
    static int anIntArray1675[] = new int[15000];
    static int anIntArray1676[] = new int[15000];
    static int anIntArray1677[] = new int[12];
    static int anIntArray1678[] = new int[10];
    static int anIntArray1679[] = new int[10];
    static int anIntArray1680[] = new int[10];
    static int xAnimOffset;
    static int yAnimOffset;
    static int zAnimOffset;
    public static boolean obj_exists;
    public static int currentCursorX;
    public static int currentCursorY;
    public static int objectsRendered;
    public static long objectKey[] = new long[15000];
    public static int SINE[];
    public static int COSINE[];
    static int hsl2rgb[];
    static int lightDecay[];

    static {
        SINE = Rasterizer3D.anIntArray1470;
        COSINE = Rasterizer3D.anIntArray1471;
        hsl2rgb = Rasterizer3D.hslToRgb;
        lightDecay = Rasterizer3D.anIntArray1469;
    }
}