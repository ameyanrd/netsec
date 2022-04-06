
// Java code for serialization and deserialization 
// of a Java object
import java.io.*;
import java.util.*;

class Demo implements java.io.Serializable {
    public int a;
    public String b;

    // Default constructor
    public Demo(int a, String b) {
        this.a = a;
        this.b = b;
    }

}

class WhitelistedObjectInputStream extends ObjectInputStream {
    public Set whitelist;

    public WhitelistedObjectInputStream(InputStream inputStream, Set wl) throws IOException {
        super(inputStream);
        whitelist = wl;
    }

    @Override
    protected Class<?> resolveClass(ObjectStreamClass cls) throws IOException, ClassNotFoundException {
        if (!whitelist.contains(cls.getName())) {
            throw new InvalidClassException("Unexpected serialized class", cls.getName());
        }
        return super.resolveClass(cls);
    }
}

class Test {
    public static void Serialize(Demo object, String filename) {
        try {
            // Saving of object in a file
            FileOutputStream file = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(file);

            // Method for serialization of object
            out.writeObject(object);

            out.close();
            file.close();

            System.out.println("Object has been serialized");

        }

        catch (IOException ex) {
            System.out.println("IOException is caught");
        }
    }

    public static Demo Deserialize(Demo object, String filename) {
        Demo object1 = null;
        try {
            // Reading the object from a file
            FileInputStream file = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(file);

            // Method for deserialization of object
            object1 = (Demo) in.readObject();

            in.close();
            file.close();

            System.out.println("Object has been deserialized ");
            System.out.println("a = " + object1.a);
            System.out.println("b = " + object1.b);
        }

        catch (IOException ex) {
            System.out.println("IOException is caught");
        }

        catch (ClassNotFoundException ex) {
            System.out.println("ClassNotFoundException is caught");
        }

        return object1;
    }

    public static Demo LookAheadDeserialize(Demo object, String filename) {
        Demo object2 = null;

        Set whitelist = new HashSet<String>(Arrays.asList(new String[]{"GoodClass1", "GoodClass2","Demo"}));

        try {
            // Reading the object from a file
            FileInputStream file = new FileInputStream(filename);
            WhitelistedObjectInputStream in = new WhitelistedObjectInputStream(file, whitelist);

            // Method for deserialization of object
            object2 = (Demo) in.readObject();

            in.close();
            file.close();

            System.out.println("Object has been deserialized ");
            System.out.println("a = " + object2.a);
            System.out.println("b = " + object2.b);
        }

        catch (IOException ex) {
            System.out.println("IOException is caught");
        }

        catch (ClassNotFoundException ex) {
            System.out.println("ClassNotFoundException is caught");
        }

        return object2;
    }

    public static void main(String[] args) {
        Demo object = new Demo(1, "geeksforgeeks");
        String filename = "file.ser";

        // Serialization
        Serialize(object, filename);

        // Deserialization
        Deserialize(object, filename);

        // Look-ahead Deserialization
        LookAheadDeserialize(object, filename);

    }
}