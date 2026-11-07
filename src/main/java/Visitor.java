public interface Visitor {

    void visit(FileNode file);

    void visit(DirectoryNode directory);

    default void beginTraversal() {
    }

    default void setLocation(String path, int depth) {
    }
}
