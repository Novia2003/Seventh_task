import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import util.ReadingUtils;

import java.io.IOException;
import java.util.*;

@RunWith(Parameterized.class)
public class ColorSamplingTest {
    private final String inputFileName;
    private final String outputFileName;

    public ColorSamplingTest(String inputFileName, String outputFileName) {
        this.inputFileName = inputFileName;
        this.outputFileName = outputFileName;
    }

    @Parameterized.Parameters
    public static List<String[]> cases() {
        return Arrays.asList(new String[]{"src/test/resources/inputForGraphWithTwoAdjacentVertices.txt",
                        "src/test/resources/outputForGraphWithTwoAdjacentVertices.txt"},
                new String[]{"src/test/resources/inputForGraphWithThreeVerticesAndTwoEdges.txt",
                        "src/test/resources/outputForGraphWithThreeVerticesAndTwoEdges.txt"},
                new String[]{"src/test/resources/inputForGraphWithFourVerticesAndThreeEdges.txt",
                        "src/test/resources/outputForGraphWithFourVerticesAndThreeEdges.txt"},
                new String[]{"src/test/resources/inputForGraphWithFourVerticesAndSixEdges.txt",
                        "src/test/resources/outputForGraphWithFourVerticesAndSixEdges.txt"},
                new String[]{"src/test/resources/inputForGraphWithEightVerticesAndElevenEdges.txt",
                        "src/test/resources/outputForGraphWithEightVerticesAndElevenEdges.txt"});
    }

    @Test
    public void assignColorVertices() throws IOException {
        Graph graph = GraphUtils.fromStr(ReadingUtils.readNumbersFromFile(inputFileName));

        ColorSampling sampling = new ColorSampling(graph);
        Map<Integer, String> map = sampling.assignColorVertices();

        List<String> list = new ArrayList<>();

        for (int i = 0; i < map.size(); i++) {
            list.add(i + ": " + map.get(i));
        }

        List<String> expectedList = ReadingUtils.readLinesFromFile(outputFileName);

        Assert.assertEquals(expectedList, list);
    }
}