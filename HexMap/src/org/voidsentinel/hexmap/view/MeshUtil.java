/**
 * 
 */
package org.voidsentinel.hexmap.view;

import java.util.ArrayList;
import java.util.List;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Triangle;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.util.BufferUtils;

/**
 * @author guipatry
 *
 */
public class MeshUtil {

	private List<Vector3f>	vertices		= new ArrayList<Vector3f>();
	private List<Vector3f>	normals		= new ArrayList<Vector3f>();
	private List<ColorRGBA>	colors		= new ArrayList<ColorRGBA>();
	private List<Integer>	triangles	= new ArrayList<Integer>();
	private List<Vector2f>	uvs			= new ArrayList<Vector2f>();

	public MeshUtil() {

	}

	public Mesh generateMesh() {
		Mesh mesh = new Mesh();

		int i = 0;
		Vector3f[] verticesArray = new Vector3f[vertices.size()];
		for (Vector3f f : vertices) {
			verticesArray[i++] = f; // Or whatever default you want.
		}

		i = 0;
		Vector3f[] normalsArray = new Vector3f[normals.size()];
		for (Vector3f f : normals) {
			normalsArray[i++] = f; // Or whatever default you want.
		}

		i = 0;
		Vector2f[] uvsArray = new Vector2f[uvs.size()];
		for (Vector2f f : uvs) {
			uvsArray[i++] = f; // Or whatever default you want.
		}

		i = 0;
		int[] trianglesArray = new int[triangles.size()];
		for (Integer f : triangles) {
			trianglesArray[i++] = f.intValue(); // Or whatever default you want.
		}

		i = 0;
		float[] colorsArray = new float[colors.size() * 4];
		for (ColorRGBA f : colors) {
			colorsArray[i++] = f.r;
			colorsArray[i++] = f.g;
			colorsArray[i++] = f.b;
			colorsArray[i++] = f.a;
		}

		mesh.setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(verticesArray));
		if (normalsArray.length > 0)
			mesh.setBuffer(Type.Normal, 3, BufferUtils.createFloatBuffer(normalsArray));
		if (colorsArray.length > 0)
			mesh.setBuffer(Type.Color, 4, colorsArray);
		if (uvsArray.length > 0)
			mesh.setBuffer(Type.TexCoord, 2, BufferUtils.createFloatBuffer(uvsArray));
		mesh.setBuffer(Type.Index, 3, BufferUtils.createIntBuffer(trianglesArray));
		mesh.setStatic();

		mesh.updateBound();

		System.out.println("Creation    : " + mesh.getId());
		System.out.println("  Vertices  : " + verticesArray.length);
		System.out.println("  Colors    : " + colorsArray.length / 4);
		System.out.println("  Normals   : " + normalsArray.length);
		System.out.println("  UVs       : " + uvsArray.length);
		System.out.println("  Triangles : " + trianglesArray.length / 3);

		System.out.println(mesh.getBound().toString());

		return mesh;
	}

	/**
	 * update the given mesh with the data stored into the MeshUtil. This can be used to update a mesh, after recalculating some data
	 * @param mesh the mesh to update
	 * @return
	 */
	public Mesh generateMesh(Mesh mesh) {
		int i = 0;
		Vector3f[] verticesArray = new Vector3f[vertices.size()];
		for (Vector3f f : vertices) {
			verticesArray[i++] = f; // Or whatever default you want.
		}

		i = 0;
		Vector3f[] normalsArray = new Vector3f[normals.size()];
		for (Vector3f f : normals) {
			normalsArray[i++] = f; // Or whatever default you want.
		}

		i = 0;
		Vector2f[] uvsArray = new Vector2f[uvs.size()];
		for (Vector2f f : uvs) {
			uvsArray[i++] = f; // Or whatever default you want.
		}

		i = 0;
		int[] trianglesArray = new int[triangles.size()];
		for (Integer f : triangles) {
			trianglesArray[i++] = f.intValue(); // Or whatever default you want.
		}

		i = 0;
		float[] colorsArray = new float[colors.size() * 4];
		for (ColorRGBA f : colors) {
			colorsArray[i++] = f.r;
			colorsArray[i++] = f.g;
			colorsArray[i++] = f.b;
			colorsArray[i++] = f.a;
		}
		
		if (verticesArray.length > 0)
			mesh.setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(verticesArray));
		if (normalsArray.length > 0)
			mesh.setBuffer(Type.Normal, 3, BufferUtils.createFloatBuffer(normalsArray));
		if (colorsArray.length > 0)
			mesh.setBuffer(Type.Color, 4, colorsArray);
		if (uvsArray.length > 0)
			mesh.setBuffer(Type.TexCoord, 2, BufferUtils.createFloatBuffer(uvsArray));
		if (trianglesArray.length > 0)
			mesh.setBuffer(Type.Index, 3, BufferUtils.createIntBuffer(trianglesArray));

		mesh.setStatic();
		mesh.updateBound();

		System.out.println("Update      : " + mesh.getId());
		System.out.println("  Vertices  : " + verticesArray.length);
		System.out.println("  Colors    : " + colorsArray.length / 4);
		System.out.println("  Normals   : " + normalsArray.length);
		System.out.println("  UVs       : " + uvsArray.length);
		System.out.println("  Triangles : " + trianglesArray.length / 3);

		System.out.println(mesh.getBound().toString());

		return mesh;
	}

	public int getVerticeCount() {
		return vertices.size();
	}

	public float[] getColorArray() {
		int i = 0;
		float[] colorsArray = new float[colors.size() * 4];
		for (ColorRGBA f : colors) {
			colorsArray[i++] = f.r;
			colorsArray[i++] = f.g;
			colorsArray[i++] = f.b;
			colorsArray[i++] = f.a;
		}
		return colorsArray;

	}

	public void addTriangle(Vector3f v1, Vector3f v2, Vector3f v3) {
		int index = vertices.size();
		Vector3f normal = new Vector3f();
		Triangle.computeTriangleNormal(v1, v2, v3, normal);
		vertices.add(v1);
		vertices.add(v2);
		vertices.add(v3);
		normals.add(normal);
		normals.add(normal);
		normals.add(normal);
		triangles.add(index);
		triangles.add(index + 1);
		triangles.add(index + 2);
	}

	/**
	 * Add a quad (as 2 triangles) to the mesh. Vertices are given as the bottom 2
	 * then the top 2 (in the Z axis)
	 * 
	 * @param v1
	 * @param v2
	 * @param v3
	 * @param v4
	 */
	public void addQuad(Vector3f v1, Vector3f v2, Vector3f v3, Vector3f v4) {
		Vector3f normal = new Vector3f();

		int index = vertices.size();
		Triangle.computeTriangleNormal(v1, v2, v3, normal);
		vertices.add(v1);
		vertices.add(v2);
		vertices.add(v3);
		vertices.add(v4);
		normals.add(normal);
		normals.add(normal);
		normals.add(normal);
		normals.add(normal);
		addTriangle(index, index + 3, index + 2);
		addTriangle(index + 1, index + 3, index);
	}

	public void addQuadColors(ColorRGBA c1, ColorRGBA c2, ColorRGBA c3, ColorRGBA c4) {
		colors.add(c1);
		colors.add(c2);
		colors.add(c3);
		colors.add(c4);
	}

	public void addVertice(Vector3f v1) {
		vertices.add(v1);
	}

	public void addVertices(Vector3f v1, Vector3f v2, Vector3f v3, boolean triangle) {
		int index = vertices.size();
		addVertice(v1);
		addVertice(v2);
		addVertice(v3);
		if (triangle) {
			triangles.add(index);
			triangles.add(index + 1);
			triangles.add(index + 2);
		}
	}

	public void addNormal(Vector3f v1) {
		normals.add(v1);
	}

	public void addNormals(Vector3f v1, Vector3f v2, Vector3f v3) {
		normals.add(v1);
		normals.add(v2);
		normals.add(v3);
	}

	public void addUV(Vector2f v1) {
		uvs.add(v1);
	}

	public void addUVs(Vector2f v1, Vector2f v2, Vector2f v3) {
		uvs.add(v1);
		uvs.add(v2);
		uvs.add(v3);
	}

	public void addColor(ColorRGBA c1) {
		colors.add(c1);
	}

	public void addColors(ColorRGBA c1, ColorRGBA c2, ColorRGBA c3) {
		colors.add(c1);
		colors.add(c2);
		colors.add(c3);
	}

	public void addTriangle(int i1, int i2, int i3) {
		triangles.add(i1);
		triangles.add(i2);
		triangles.add(i3);
	}

	/**
	 * @return the vertices
	 */
	public List<Vector3f> getVertices() {
		return vertices;
	}

	/**
	 * @return the normals
	 */
	public List<Vector3f> getNormals() {
		return normals;
	}

	/**
	 * @return the colors
	 */
	public List<ColorRGBA> getColors() {
		return colors;
	}

	/**
	 * @return the triangles
	 */
	public List<Integer> getTriangles() {
		return triangles;
	}

}
