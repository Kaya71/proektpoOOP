import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class SVGParser {

    private ArrayList<Shape> shapes;  // list to hold shapes read from file

    public SVGParser() {
        shapes = new ArrayList<Shape>();
    }

    public void parseFile(String fileName) {
        try {
            File file = new File(fileName);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.contains("<circle")) {
                    Circle circle = parseCircle(line);
                    if (circle != null) {
                        shapes.add(circle);
                    }
                } else if (line.contains("<rect")) {
                    Rectangle rectangle = parseRectangle(line);
                    if (rectangle != null) {
                        shapes.add(rectangle);
                    }
                } else if (line.contains("<line")) {
                    Line lineShape = parseLine(line);
                    if (lineShape != null) {
                        shapes.add(lineShape);
                    }
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void addShape(Shape shape) {
        shapes.add(shape);
    }

    public void removeShape(int index) {
        shapes.remove(index);
    }

    public void modifyShape(int index, Shape newShape) {
        shapes.set(index, newShape);
    }

    public void saveToFile(String fileName) {
        try {
            FileWriter fileWriter = new FileWriter(fileName);
            fileWriter.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n");
            fileWriter.write("<svg xmlns=\"http://www.w3.org/2000/svg\">\n");
            for (Shape shape : shapes) {
                fileWriter.write(shape.toSVGString() + "\n");
            }
            fileWriter.write("</svg>\n");
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Circle parseCircle(String line) {
        int cxIndex = line.indexOf("cx=\"") + 4;
        int cyIndex = line.indexOf("cy=\"") + 4;
        int rIndex = line.indexOf("r=\"") + 3;
        int endIndex = line.indexOf("\"", rIndex);
        if (cxIndex < 4 || cyIndex < 4 || rIndex < 3 || endIndex < 0) {
            return null;
        }
        double cx = Double.parseDouble(line.substring(cxIndex, line.indexOf("\"", cxIndex)));
        double cy = Double.parseDouble(line.substring(cyIndex, line.indexOf("\"", cyIndex)));
        double r = Double.parseDouble(line.substring(rIndex, endIndex));
        return new Circle(cx, cy, r);
    }

    private void parseRectangle(String line) {
        int xIndex = line.indexOf("x=\"") + 3;
        int yIndex = line.indexOf("y=\"") + 3;
        int widthIndex = line.indexOf("width=\"") + 7;
        int heightIndex = line.indexOf("height=\"") + 8;
        int endIndex = line.indexOf("\"", heightIndex);
        if (xIndex < 3 || yIndex < 3 || widthIndex < 7 || heightIndex < 8 || endIndex < 0) {

        }
        double x = Double.parseDouble(line.substring(xIndex, line.indexOf("\"", xIndex)));
        double y = Double.parseDouble(line.substring(yIndex, line.indexOf("\"", yIndex)));
        double width = Double.parseDouble(line.substring(widthIndex, line.indexOf("\"", widthIndex)));
        double height = Double.parseDouble(line.substring(heightIndex, line.indexOf("\"", heightIndex)));
        Rectangle rectangle = new Rectangle(x, y, width, height);
        addShape(rectangle);
    }

    private Circle parseCircle(String line) {
        Circle circle = null;
        String[] attributes = getShapeAttributes(line);
        if (attributes.length == 3) {
            try {
                double cx = Double.parseDouble(attributes[0]);
                double cy = Double.parseDouble(attributes[1]);
                double r = Double.parseDouble(attributes[2]);
                circle = new Circle(cx, cy, r);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return circle;
    }

    private Rectangle parseRectangle(String line) {
        Rectangle rectangle = null;
        String[] attributes = getShapeAttributes(line);
        if (attributes.length == 4) {
            try {
                double x = Double.parseDouble(attributes[0]);
                double y = Double.parseDouble(attributes[1]);
                double width = Double.parseDouble(attributes[2]);
                double height = Double.parseDouble(attributes[3]);
                rectangle = new Rectangle(x, y, width, height);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return rectangle;
    }

    private Line parseLine(String line) {
        Line lineShape = null;
        String[] attributes = getShapeAttributes(line);
        if (attributes.length == 4) {
            try {
                double x1 = Double.parseDouble(attributes[0]);
                double y1 = Double.parseDouble(attributes[1]);
                double x2 = Double.parseDouble(attributes[2]);
                double y2 = Double.parseDouble(attributes[3]);
                lineShape = new Line(x1, y1, x2, y2);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return lineShape;
    }

    private String[] getShapeAttributes(String line) {
        ArrayList<String> attributes = new ArrayList<String>();
        int startIndex = 0;
        int endIndex = 0;
        while (startIndex != -1 && endIndex != -1) {
            startIndex = line.indexOf("\"", endIndex) + 1;
            if (startIndex != -1) {
                endIndex = line.indexOf("\"", startIndex);
                if (endIndex != -1) {
                    attributes.add(line.substring(startIndex, endIndex));
                }
            }
        }
        return attributes.toArray(new String[attributes.size()]);
    }
}
public interface Shape {
    String toSVGString();
}

// Circle class
public class Circle implements Shape {
