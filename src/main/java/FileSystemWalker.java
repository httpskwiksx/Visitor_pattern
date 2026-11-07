public class FileSystemWalker {

    public void walk(Element root, Visitor visitor) {
        if (root == null || visitor == null) {
            throw new IllegalArgumentException(
                    "Root and visitor cannot be null"
            );
        }

        visitor.beginTraversal();
        traverse(root, visitor, "", 0);
    }

    private void traverse(Element element, Visitor visitor,
                          String path, int depth) {

        visitor.setLocation(path, depth);
        element.accept(visitor);

        for (Element child : element.getChildren()) {

            String childPath = path.isEmpty()
                    ? child.getName()
                    : path + "/" + child.getName();

            traverse(child, visitor, childPath, depth + 1);
        }
    }
}

