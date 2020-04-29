package dji.midware.ar.min3d.core;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.ar.min3d.interfaces.IObject3dContainer;
import dji.midware.ar.min3d.vos.Color4;
import dji.midware.ar.min3d.vos.Number3d;
import dji.midware.ar.min3d.vos.RenderType;
import dji.midware.ar.min3d.vos.ShadeModel;
import java.util.ArrayList;
import javax.microedition.khronos.opengles.GL10;

@EXClassNullAway
public class Object3d {
    protected boolean _animationEnabled = false;
    protected ArrayList<Object3d> _children;
    private boolean _colorMaterialEnabled = false;
    private Color4 _defaultColor = new Color4();
    private boolean _doubleSidedEnabled = false;
    protected FacesBufferedList _faces;
    private boolean _ignoreFaces = false;
    private volatile boolean _isVisible = true;
    private boolean _lightingEnabled = true;
    private boolean _lineSmoothing = false;
    private float _lineWidth = 1.0f;
    private String _name;
    private boolean _normalsEnabled = true;
    private IObject3dContainer _parent;
    private float _pointSize = 3.0f;
    private boolean _pointSmoothing = true;
    private Number3d _position = new Number3d(0.0f, 0.0f, 0.0f);
    private RenderType _renderType = RenderType.TRIANGLES;
    private Number3d _rotation = new Number3d(0.0f, 0.0f, 0.0f);
    private Number3d _scale = new Number3d(1.0f, 1.0f, 1.0f);
    private Scene _scene;
    private ShadeModel _shadeModel = ShadeModel.SMOOTH;
    protected TextureList _textures;
    private boolean _texturesEnabled = true;
    private boolean _vertexColorsEnabled = true;
    protected Vertices _vertices;

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.midware.ar.min3d.core.Vertices.<init>(int, java.lang.Boolean, java.lang.Boolean, java.lang.Boolean):void
     arg types: [int, int, int, int]
     candidates:
      dji.midware.ar.min3d.core.Vertices.<init>(dji.midware.ar.min3d.core.Number3dBufferList, dji.midware.ar.min3d.core.UvBufferList, dji.midware.ar.min3d.core.Number3dBufferList, dji.midware.ar.min3d.core.Color4BufferList):void
      dji.midware.ar.min3d.core.Vertices.<init>(int, java.lang.Boolean, java.lang.Boolean, java.lang.Boolean):void */
    public Object3d(int $maxVertices, int $maxFaces) {
        this._vertices = new Vertices($maxVertices, (Boolean) true, (Boolean) true, (Boolean) true);
        this._faces = new FacesBufferedList($maxFaces);
        this._textures = new TextureList();
    }

    public Object3d(int $maxVertices, int $maxFaces, Boolean $useUvs, Boolean $useNormals, Boolean $useVertexColors) {
        this._vertices = new Vertices($maxVertices, $useUvs, $useNormals, $useVertexColors);
        this._faces = new FacesBufferedList($maxFaces);
        this._textures = new TextureList();
    }

    public Object3d(Vertices $vertices, FacesBufferedList $faces, TextureList $textures) {
        this._vertices = $vertices;
        this._faces = $faces;
        this._textures = $textures;
    }

    public Vertices vertices() {
        return this._vertices;
    }

    public FacesBufferedList faces() {
        return this._faces;
    }

    public TextureList textures() {
        return this._textures;
    }

    public boolean isVisible() {
        return this._isVisible;
    }

    public void isVisible(Boolean $b) {
        this._isVisible = $b.booleanValue();
    }

    public boolean doubleSidedEnabled() {
        return this._doubleSidedEnabled;
    }

    public void doubleSidedEnabled(boolean $b) {
        this._doubleSidedEnabled = $b;
    }

    public boolean colorMaterialEnabled() {
        return this._colorMaterialEnabled;
    }

    public boolean lightingEnabled() {
        return this._lightingEnabled;
    }

    public void lightingEnabled(boolean $b) {
        this._lightingEnabled = $b;
    }

    public void colorMaterialEnabled(boolean $b) {
        this._colorMaterialEnabled = $b;
    }

    public boolean animationEnabled() {
        return this._animationEnabled;
    }

    public void animationEnabled(boolean $b) {
        this._animationEnabled = $b;
    }

    public boolean vertexColorsEnabled() {
        return this._vertexColorsEnabled;
    }

    public void vertexColorsEnabled(Boolean $b) {
        this._vertexColorsEnabled = $b.booleanValue();
    }

    public boolean texturesEnabled() {
        return this._texturesEnabled;
    }

    public void texturesEnabled(Boolean $b) {
        this._texturesEnabled = $b.booleanValue();
    }

    public boolean normalsEnabled() {
        return this._normalsEnabled;
    }

    public void normalsEnabled(boolean $b) {
        this._normalsEnabled = $b;
    }

    public boolean ignoreFaces() {
        return this._ignoreFaces;
    }

    public void ignoreFaces(boolean $b) {
        this._ignoreFaces = $b;
    }

    public RenderType renderType() {
        return this._renderType;
    }

    public void renderType(RenderType $type) {
        this._renderType = $type;
    }

    public ShadeModel shadeModel() {
        return this._shadeModel;
    }

    public void shadeModel(ShadeModel $shadeModel) {
        this._shadeModel = $shadeModel;
    }

    public Number3dBufferList points() {
        return this._vertices.points();
    }

    public UvBufferList uvs() {
        return this._vertices.uvs();
    }

    public Number3dBufferList normals() {
        return this._vertices.normals();
    }

    public Color4BufferList colors() {
        return this._vertices.colors();
    }

    public boolean hasUvs() {
        return this._vertices.hasUvs();
    }

    public boolean hasNormals() {
        return this._vertices.hasNormals();
    }

    public boolean hasVertexColors() {
        return this._vertices.hasColors();
    }

    public void clear() {
        if (vertices().points() != null) {
            vertices().points().clear();
        }
        if (vertices().uvs() != null) {
            vertices().uvs().clear();
        }
        if (vertices().normals() != null) {
            vertices().normals().clear();
        }
        if (vertices().colors() != null) {
            vertices().colors().clear();
        }
        if (this._textures != null) {
            this._textures.clear();
        }
        if (parent() != null) {
            parent().removeChild(this);
        }
    }

    public Color4 defaultColor() {
        return this._defaultColor;
    }

    public void defaultColor(Color4 color) {
        this._defaultColor = color;
    }

    public Number3d position() {
        return this._position;
    }

    public Number3d rotation() {
        return this._rotation;
    }

    public Number3d scale() {
        return this._scale;
    }

    public float pointSize() {
        return this._pointSize;
    }

    public void pointSize(float $n) {
        this._pointSize = $n;
    }

    public boolean pointSmoothing() {
        return this._pointSmoothing;
    }

    public void pointSmoothing(boolean $b) {
        this._pointSmoothing = $b;
    }

    public float lineWidth() {
        return this._lineWidth;
    }

    public void lineWidth(float $n) {
        this._lineWidth = $n;
    }

    public boolean lineSmoothing() {
        return this._lineSmoothing;
    }

    public void lineSmoothing(boolean $b) {
        this._lineSmoothing = $b;
    }

    public String name() {
        return this._name;
    }

    public void name(String $s) {
        this._name = $s;
    }

    public IObject3dContainer parent() {
        return this._parent;
    }

    /* access modifiers changed from: package-private */
    public void parent(IObject3dContainer $container) {
        this._parent = $container;
    }

    /* access modifiers changed from: package-private */
    public void scene(Scene $scene) {
        this._scene = $scene;
    }

    /* access modifiers changed from: package-private */
    public Scene scene() {
        return this._scene;
    }

    public Boolean customRenderer(GL10 gl) {
        return false;
    }

    public Object3d clone() {
        Object3d clone = new Object3d(this._vertices.clone(), this._faces.clone(), this._textures);
        clone.position().x = position().x;
        clone.position().y = position().y;
        clone.position().z = position().z;
        clone.rotation().x = rotation().x;
        clone.rotation().y = rotation().y;
        clone.rotation().z = rotation().z;
        clone.scale().x = scale().x;
        clone.scale().y = scale().y;
        clone.scale().z = scale().z;
        return clone;
    }
}
