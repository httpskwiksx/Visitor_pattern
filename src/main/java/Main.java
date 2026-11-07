public class Main {

    public static void main(String[] args) {

        // Create the file system
        DirectoryNode root = new DirectoryNode("root");
        DirectoryNode documents = new DirectoryNode("documents");
        DirectoryNode archive = new DirectoryNode("archive");
        DirectoryNode images = new DirectoryNode("images");
        DirectoryNode empty = new DirectoryNode("empty");

        // Add files and directories
        root.addChild(new FileNode("notes.txt", 100));
        root.addChild(documents);
        root.addChild(images);
        root.addChild(empty);

        documents.addChild(new FileNode("report.pdf", 500));
        documents.addChild(archive);

        archive.addChild(new FileNode("old-report.pdf", 200));
        images.addChild(new FileNode("photo.png", 800));

        // Create the walker
        FileSystemWalker walker = new FileSystemWalker();

        // 1. TotalSizeVisitor
        TotalSizeVisitor sizeVisitor = new TotalSizeVisitor();
        walker.walk(root, sizeVisitor);

        System.out.println("Total size: "
                + sizeVisitor.getTotalSize() + " bytes");

        // 2. SearchVisitor:
        SearchVisitor searchVisitor = new SearchVisitor("report", 200);
        walker.walk(root, searchVisitor);

        System.out.println("Matching files: "
                + searchVisitor.getMatchingPaths());

        // 3. StatisticsVisitor:
        StatisticsVisitor statisticsVisitor = new StatisticsVisitor();
        walker.walk(root, statisticsVisitor);

        System.out.println("Files: "
                + statisticsVisitor.getFileCount());

        System.out.println("Directories: "
                + statisticsVisitor.getDirectoryCount());

        System.out.println("Maximum depth: "
                + statisticsVisitor.getMaxDepth());
    }
}
