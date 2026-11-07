public class StatisticsVisitor implements Visitor {

    private int fileCount = 0;
    private int directoryCount = 0;
    private int maxDepth = 0;
    private int currentDepth = 0;

    @Override
    public void visit(FileNode file) {
        fileCount++;
        maxDepth = Math.max(maxDepth, currentDepth);
    }

    @Override
    public void visit(DirectoryNode directory) {
        directoryCount++;
        maxDepth = Math.max(maxDepth, currentDepth);
    }

    @Override
    public void beginTraversal() {
        fileCount = 0;
        directoryCount = 0;
        maxDepth = 0;
        currentDepth = 0;
    }

    @Override
    public void setLocation(String path, int depth) {
        currentDepth = depth;
    }

    public int getFileCount() {
        return fileCount;
    }

    public int getDirectoryCount() {
        return directoryCount;
    }

    public int getMaxDepth() {
        return maxDepth;
    }
}
