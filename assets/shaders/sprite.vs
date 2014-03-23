attribute vec4 vPosition;
attribute vec4 vTextureCoordinate;

varying vec2 textureCoordinate;

void main()
{
    textureCoordinate = vTextureCoordinate.xy;
    gl_Position = vPosition;
}