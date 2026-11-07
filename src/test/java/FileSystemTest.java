import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FileSystemTest {
    //T1:
    @Test
    void testDoubleDispatch() {

        int[] calls = {0, 0};

        Visitor visitor = new Visitor() {

            @Override
            public void visit(FileNode file) {
                calls[0]++;
            }

            @Override
            public void visit(DirectoryNode directory) {
                calls[1]++;
            }
        };

        FileNode file = new FileNode("notes.txt", 100);
        DirectoryNode directory = new DirectoryNode("root");

        file.accept(visitor);
        directory.accept(visitor);

        assertEquals(1, calls[0]);
        assertEquals(1, calls[1]);
    }
    //T2:
    @Test
    void testTotalSizeNested() {

        DirectoryNode root = new DirectoryNode("root");
        DirectoryNode documents = new DirectoryNode("documents");
        DirectoryNode archive = new DirectoryNode("archive");

        root.addChild(new FileNode("notes.txt", 100));
        root.addChild(documents);

        documents.addChild(new FileNode("report.pdf", 250));
        documents.addChild(archive);

        archive.addChild(new FileNode("old.txt", 400));

        TotalSizeVisitor visitor = new TotalSizeVisitor();
        FileSystemWalker walker = new FileSystemWalker();

        walker.walk(root, visitor);

        assertEquals(750, visitor.getTotalSize());
    }
    //T3:
    @Test
    void testSearchWithTwoConditions() {

        DirectoryNode root = new DirectoryNode("root");

        root.addChild(new FileNode("report.txt", 200));
        root.addChild(new FileNode("report-small.txt", 100));
        root.addChild(new FileNode("notes.txt", 500));

        SearchVisitor visitor = new SearchVisitor("report", 200);
        FileSystemWalker walker = new FileSystemWalker();

        walker.walk(root, visitor);

        assertEquals(
                java.util.List.of("report.txt"),
                visitor.getMatchingPaths()
        );
    }
    //T4:

    @Test
    void testSearchPathsAndSorting() {

        DirectoryNode root = new DirectoryNode("root");
        DirectoryNode zebra = new DirectoryNode("zebra");
        DirectoryNode alpha = new DirectoryNode("alpha");

        root.addChild(zebra);
        root.addChild(alpha);

        zebra.addChild(new FileNode("report-z.txt", 300));
        alpha.addChild(new FileNode("report-a.txt", 200));
        root.addChild(new FileNode("report-root.txt", 400));

        SearchVisitor visitor = new SearchVisitor("report", 100);
        FileSystemWalker walker = new FileSystemWalker();

        walker.walk(root, visitor);

        assertEquals(
                java.util.List.of(
                        "alpha/report-a.txt",
                        "report-root.txt",
                        "zebra/report-z.txt"
                ),
                visitor.getMatchingPaths()
        );
    }
    //T5:

    @Test
    void testStatisticsCountsAndDepth() {

        DirectoryNode root = new DirectoryNode("root");
        DirectoryNode documents = new DirectoryNode("documents");
        DirectoryNode archive = new DirectoryNode("archive");
        DirectoryNode empty = new DirectoryNode("empty");

        root.addChild(new FileNode("notes.txt", 100));
        root.addChild(documents);
        root.addChild(empty);

        documents.addChild(new FileNode("report.pdf", 500));
        documents.addChild(archive);

        archive.addChild(new FileNode("old.txt", 200));

        StatisticsVisitor visitor = new StatisticsVisitor();
        FileSystemWalker walker = new FileSystemWalker();

        walker.walk(root, visitor);

        assertEquals(3, visitor.getFileCount());
        assertEquals(4, visitor.getDirectoryCount());
        assertEquals(3, visitor.getMaxDepth());
    }
    //T6:

    @Test
    void testEmptyDirectoryAndNoMatches() {

        DirectoryNode root = new DirectoryNode("root");
        FileSystemWalker walker = new FileSystemWalker();

        TotalSizeVisitor sizeVisitor = new TotalSizeVisitor();
        walker.walk(root, sizeVisitor);

        assertEquals(0, sizeVisitor.getTotalSize());

        StatisticsVisitor statisticsVisitor = new StatisticsVisitor();
        walker.walk(root, statisticsVisitor);

        assertEquals(0, statisticsVisitor.getFileCount());
        assertEquals(1, statisticsVisitor.getDirectoryCount());
        assertEquals(0, statisticsVisitor.getMaxDepth());

        SearchVisitor searchVisitor = new SearchVisitor("report", 100);
        walker.walk(root, searchVisitor);

        assertEquals(
                java.util.List.of(),
                searchVisitor.getMatchingPaths()
        );
    }
    //7:
    @Test
    void testRepeatedVisitorExecutions() {

        DirectoryNode root = new DirectoryNode("root");
        DirectoryNode docs = new DirectoryNode("docs");

        root.addChild(new FileNode("report.txt", 120));
        root.addChild(docs);
        docs.addChild(new FileNode("notes.txt", 50));

        DirectoryNode secondRoot = new DirectoryNode("secondRoot");
        secondRoot.addChild(new FileNode("draft.txt", 25));

        FileSystemWalker walker = new FileSystemWalker();

        TotalSizeVisitor size = new TotalSizeVisitor();
        SearchVisitor search = new SearchVisitor("report", 100);
        StatisticsVisitor stats = new StatisticsVisitor();

        walker.walk(root, size);
        walker.walk(root, search);
        walker.walk(root, stats);

        assertEquals(170, size.getTotalSize());
        assertEquals(java.util.List.of("report.txt"),
                search.getMatchingPaths());
        assertEquals(2, stats.getFileCount());
        assertEquals(2, stats.getDirectoryCount());
        assertEquals(2, stats.getMaxDepth());

        walker.walk(secondRoot, size);
        walker.walk(secondRoot, search);
        walker.walk(secondRoot, stats);

        assertEquals(25, size.getTotalSize());
        assertEquals(java.util.List.of(),
                search.getMatchingPaths());
        assertEquals(1, stats.getFileCount());
        assertEquals(1, stats.getDirectoryCount());
        assertEquals(1, stats.getMaxDepth());
    }
    //T8:

    @Test
    void testTreeIntegrity() {

        assertThrows(IllegalArgumentException.class, () ->
                new FileNode("invalid.txt", -10)
        );

        DirectoryNode root = new DirectoryNode("root");
        root.addChild(new FileNode("notes.txt", 100));

        assertThrows(IllegalArgumentException.class, () ->
                root.addChild(new FileNode("notes.txt", 200))
        );

        FileNode sharedFile = new FileNode("shared.txt", 50);

        DirectoryNode first = new DirectoryNode("first");
        DirectoryNode second = new DirectoryNode("second");

        first.addChild(sharedFile);

        assertThrows(IllegalArgumentException.class, () ->
                second.addChild(sharedFile)
        );

        DirectoryNode parent = new DirectoryNode("parent");
        DirectoryNode child = new DirectoryNode("child");

        parent.addChild(child);

        assertThrows(IllegalArgumentException.class, () ->
                child.addChild(parent)
        );

        DirectoryNode documents = new DirectoryNode("documents");
        root.addChild(documents);
        documents.addChild(new FileNode("report.pdf", 300));

        TotalSizeVisitor visitor = new TotalSizeVisitor();
        FileSystemWalker walker = new FileSystemWalker();

        walker.walk(root, visitor);


        assertEquals(400, visitor.getTotalSize());
    }

}
