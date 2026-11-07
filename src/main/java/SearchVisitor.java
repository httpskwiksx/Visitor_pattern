import java.util.List;
import java.util.Locale;
import java.util.TreeSet;

public class SearchVisitor implements Visitor {

    private final String keyword;
    private final long minSize;

    private final TreeSet<String> matchingPaths = new TreeSet<>();
    private String currentPath = "";

    public SearchVisitor(String keyword, long minSize) {
        if (keyword == null || minSize < 0) {
            throw new IllegalArgumentException("Invalid search parameters");
        }

        this.keyword = keyword.toLowerCase(Locale.ROOT);
        this.minSize = minSize;
    }

    @Override
    public void visit(FileNode file) {
        boolean nameMatches = file.getName()
                .toLowerCase(Locale.ROOT)
                .contains(keyword);

        boolean sizeMatches = file.getSize() >= minSize;

        if (nameMatches && sizeMatches) {
            matchingPaths.add(currentPath);
        }
    }

    @Override
    public void visit(DirectoryNode directory) {
        // Only files are included in search results.
    }

    @Override
    public void beginTraversal() {
        matchingPaths.clear();
        currentPath = "";
    }

    @Override
    public void setLocation(String path, int depth) {
        currentPath = path;
    }

    public List<String> getMatchingPaths() {
        return List.copyOf(matchingPaths);
    }
}
