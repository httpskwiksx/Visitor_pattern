import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

public class DirectoryNode implements Element {

    private final String name;
    private final List<Element> children = new ArrayList<>();

    private static final Map<Element, DirectoryNode> owners =
            new IdentityHashMap<>();

    public DirectoryNode(String name) {
        if (name == null || name.isBlank()
                || name.contains("/") || name.contains("\\")) {
            throw new IllegalArgumentException("Invalid directory name");
        }

        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public void addChild(Element child) {
        if (child == null) {
            throw new IllegalArgumentException("Child cannot be null");
        }

        synchronized (owners) {

            for (Element existing : children) {
                if (existing.getName().equals(child.getName())) {
                    throw new IllegalArgumentException("Duplicate child name");
                }
            }

            if (owners.containsKey(child)) {
                throw new IllegalArgumentException("Child already has a parent");
            }

            DirectoryNode ancestor = this;

            while (ancestor != null) {
                if (ancestor == child) {
                    throw new IllegalArgumentException("Cycle detected");
                }
                ancestor = owners.get(ancestor);
            }

            children.add(child);
            owners.put(child, this);
        }
    }

    public List<Element> getChildren() {
        return List.copyOf(children);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
