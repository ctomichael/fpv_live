package dji.midware.ar.min3d.animation;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.ar.min3d.core.FacesBufferedList;
import dji.midware.ar.min3d.core.Object3d;
import dji.midware.ar.min3d.core.TextureList;
import dji.midware.ar.min3d.core.Vertices;

@EXClassNullAway
public class AnimationObject3d extends Object3d {
    private int currentFrameIndex;
    private String currentFrameName;
    private long currentTime;
    private float fps = 70.0f;
    private KeyFrame[] frames;
    private float interpolation;
    private boolean isPlaying;
    private boolean loop = false;
    private int loopStartIndex;
    private int numFrames;
    private long startTime;
    private boolean updateVertices = true;

    public AnimationObject3d(int $maxVertices, int $maxFaces, int $numFrames) {
        super($maxVertices, $maxFaces);
        this.numFrames = $numFrames;
        this.frames = new KeyFrame[this.numFrames];
        this.currentFrameIndex = 0;
        this.isPlaying = false;
        this.interpolation = 0.0f;
        this._animationEnabled = true;
    }

    public AnimationObject3d(Vertices $vertices, FacesBufferedList $faces, TextureList $textures, KeyFrame[] $frames) {
        super($vertices, $faces, $textures);
        this.numFrames = $frames.length;
        this.frames = $frames;
    }

    public int getCurrentFrame() {
        return this.currentFrameIndex;
    }

    public void addFrame(KeyFrame frame) {
        KeyFrame[] keyFrameArr = this.frames;
        int i = this.currentFrameIndex;
        this.currentFrameIndex = i + 1;
        keyFrameArr[i] = frame;
    }

    public void setFrames(KeyFrame[] frames2) {
        this.frames = frames2;
    }

    public void play() {
        this.startTime = System.currentTimeMillis();
        this.isPlaying = true;
        this.currentFrameName = null;
        this.loop = false;
    }

    public void play(String name) {
        this.currentFrameIndex = 0;
        this.currentFrameName = name;
        int i = 0;
        while (true) {
            if (i >= this.numFrames) {
                break;
            } else if (this.frames[i].getName().equals(name)) {
                this.currentFrameIndex = i;
                this.loopStartIndex = i;
                break;
            } else {
                i++;
            }
        }
        this.startTime = System.currentTimeMillis();
        this.isPlaying = true;
    }

    public void play(String name, boolean loop2) {
        this.loop = loop2;
        play(name);
    }

    public void stop() {
        this.isPlaying = false;
        this.currentFrameIndex = 0;
    }

    public void pause() {
        this.isPlaying = false;
    }

    public void update() {
        if (this.isPlaying && this.updateVertices) {
            this.currentTime = System.currentTimeMillis();
            KeyFrame currentFrame = this.frames[this.currentFrameIndex];
            KeyFrame nextFrame = this.frames[(this.currentFrameIndex + 1) % this.numFrames];
            if (this.currentFrameName == null || this.currentFrameName.equals(currentFrame.getName())) {
                float[] currentVerts = currentFrame.getVertices();
                float[] nextVerts = nextFrame.getVertices();
                float[] currentNormals = currentFrame.getNormals();
                float[] nextNormals = nextFrame.getNormals();
                int numVerts = currentVerts.length;
                float[] interPolatedVerts = new float[numVerts];
                float[] interPolatedNormals = new float[numVerts];
                for (int i = 0; i < numVerts; i += 3) {
                    interPolatedVerts[i] = currentVerts[i] + (this.interpolation * (nextVerts[i] - currentVerts[i]));
                    interPolatedVerts[i + 1] = currentVerts[i + 1] + (this.interpolation * (nextVerts[i + 1] - currentVerts[i + 1]));
                    interPolatedVerts[i + 2] = currentVerts[i + 2] + (this.interpolation * (nextVerts[i + 2] - currentVerts[i + 2]));
                    interPolatedNormals[i] = currentNormals[i] + (this.interpolation * (nextNormals[i] - currentNormals[i]));
                    interPolatedNormals[i + 1] = currentNormals[i + 1] + (this.interpolation * (nextNormals[i + 1] - currentNormals[i + 1]));
                    interPolatedNormals[i + 2] = currentNormals[i + 2] + (this.interpolation * (nextNormals[i + 2] - currentNormals[i + 2]));
                }
                this.interpolation += (this.fps * ((float) (this.currentTime - this.startTime))) / 1000.0f;
                vertices().overwriteNormals(interPolatedNormals);
                vertices().overwriteVerts(interPolatedVerts);
                if (this.interpolation > 1.0f) {
                    this.interpolation = 0.0f;
                    this.currentFrameIndex++;
                    if (this.currentFrameIndex >= this.numFrames) {
                        this.currentFrameIndex = 0;
                    }
                }
                this.startTime = System.currentTimeMillis();
            } else if (!this.loop) {
                stop();
            } else {
                this.currentFrameIndex = this.loopStartIndex;
            }
        }
    }

    public float getFps() {
        return this.fps;
    }

    public void setFps(float fps2) {
        this.fps = fps2;
    }

    public Object3d clone(boolean cloneData) {
        AnimationObject3d clone = new AnimationObject3d(cloneData ? this._vertices.clone() : this._vertices, cloneData ? this._faces.clone() : this._faces, this._textures, this.frames);
        clone.position().x = position().x;
        clone.position().y = position().y;
        clone.position().z = position().z;
        clone.rotation().x = rotation().x;
        clone.rotation().y = rotation().y;
        clone.rotation().z = rotation().z;
        clone.scale().x = scale().x;
        clone.scale().y = scale().y;
        clone.scale().z = scale().z;
        clone.setFps(this.fps);
        clone.animationEnabled(animationEnabled());
        return clone;
    }

    public KeyFrame[] getClonedFrames() {
        int len = this.frames.length;
        KeyFrame[] cl = new KeyFrame[len];
        for (int i = 0; i < len; i++) {
            cl[i] = this.frames[i].clone();
        }
        return cl;
    }

    public boolean getUpdateVertices() {
        return this.updateVertices;
    }

    public void setUpdateVertices(boolean updateVertices2) {
        this.updateVertices = updateVertices2;
    }
}
