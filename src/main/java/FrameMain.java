import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.MutableNode;
import guru.nidi.graphviz.parse.Parser;
import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.bridge.*;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.svg.SVGDocument;
import util.SwingUtils;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.*;


public class FrameMain extends JFrame {

    private JPanel panelMain;
    private JSplitPane splitPanelGraph;
    private JTextArea textAreaGraphFile;
    private JButton buttonLoadGraphFromFile;
    private JButton buttonSaveGraphToFile;
    private JButton buttonCreateGraph;
    private JButton buttonSaveGraphSvgToFile;
    private JPanel panelGraphPainterContainer;

    private final JFileChooser fileChooserTxtOpen;
    private final JFileChooser fileChooserTxtSave;
    private final JFileChooser fileChooserImgSave;

    private final SvgPanel panelGraphPainter;

    private static class SvgPanel extends JPanel {
        private String svg = null;
        private GraphicsNode svgGraphicsNode = null;

        public void paint(String svg) throws IOException {
            String xmlParser = XMLResourceDescriptor.getXMLParserClassName();
            SAXSVGDocumentFactory df = new SAXSVGDocumentFactory(xmlParser);
            SVGDocument doc = df.createSVGDocument(null, new StringReader(svg));
            UserAgent userAgent = new UserAgentAdapter();
            DocumentLoader loader = new DocumentLoader(userAgent);
            BridgeContext ctx = new BridgeContext(userAgent, loader);
            ctx.setDynamicState(BridgeContext.DYNAMIC);
            GVTBuilder builder = new GVTBuilder();
            svgGraphicsNode = builder.build(ctx, doc);

            this.svg = svg;
            repaint();
        }

        @Override
        public void paintComponent(Graphics gr) {
            super.paintComponent(gr);

            if (svgGraphicsNode == null) {
                return;
            }

            double scaleX = this.getWidth() / svgGraphicsNode.getPrimitiveBounds().getWidth();
            double scaleY = this.getHeight() / svgGraphicsNode.getPrimitiveBounds().getHeight();
            double scale = Math.min(scaleX, scaleY);
            AffineTransform transform = new AffineTransform(scale, 0, 0, scale, 0, 0);
            svgGraphicsNode.setTransform(transform);
            Graphics2D g2d = (Graphics2D) gr;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            svgGraphicsNode.paint(g2d);
        }
    }


    public FrameMain() {
        this.setTitle("Графы");
        this.setContentPane(panelMain);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();

        splitPanelGraph.setBorder(null);

        fileChooserTxtOpen = new JFileChooser();
        fileChooserTxtSave = new JFileChooser();
        fileChooserImgSave = new JFileChooser();

        fileChooserTxtOpen.setCurrentDirectory(new File("C:\\Users\\ВЯЧЕСЛАВ\\ВГУ\\АиСД\\Seventh__task\\src\\main\\resources"));
        fileChooserTxtSave.setCurrentDirectory(new File("C:\\Users\\ВЯЧЕСЛАВ\\ВГУ\\АиСД\\Seventh__task\\src\\main\\resources"));
        fileChooserImgSave.setCurrentDirectory(new File("C:\\Users\\ВЯЧЕСЛАВ\\ВГУ\\АиСД\\Seventh__task\\src\\main\\resources"));
        FileFilter txtFilter = new FileNameExtensionFilter("Text files (*.txt)", "txt");
        FileFilter svgFilter = new FileNameExtensionFilter("SVG images (*.svg)", "svg");

        fileChooserTxtOpen.addChoosableFileFilter(txtFilter);
        fileChooserTxtSave.addChoosableFileFilter(txtFilter);
        fileChooserImgSave.addChoosableFileFilter(svgFilter);

        fileChooserTxtSave.setAcceptAllFileFilterUsed(false);
        fileChooserTxtSave.setDialogType(JFileChooser.SAVE_DIALOG);
        fileChooserTxtSave.setApproveButtonText("Save");



        fileChooserImgSave.setAcceptAllFileFilterUsed(false);
        fileChooserImgSave.setDialogType(JFileChooser.SAVE_DIALOG);
        fileChooserImgSave.setApproveButtonText("Save");

        panelGraphPainterContainer.setLayout(new BorderLayout());
        panelGraphPainter = new SvgPanel();
        panelGraphPainterContainer.add(new JScrollPane(panelGraphPainter));


        buttonLoadGraphFromFile.addActionListener(e -> {
            if (fileChooserTxtOpen.showOpenDialog(FrameMain.this) == JFileChooser.APPROVE_OPTION) {
                try (Scanner sc = new Scanner(fileChooserTxtOpen.getSelectedFile())) {
                    sc.useDelimiter("\\Z");
                    textAreaGraphFile.setText(sc.next());
                } catch (Exception exc) {
                    SwingUtils.showErrorMessageBox(exc);
                }
            }
        });

        buttonSaveGraphToFile.addActionListener(e -> {
            if (fileChooserTxtSave.showSaveDialog(FrameMain.this) == JFileChooser.APPROVE_OPTION) {
                String filename = fileChooserTxtSave.getSelectedFile().getPath();
                if (!filename.toLowerCase().endsWith(".txt")) {
                    filename += ".txt";
                }
                try (FileWriter wr = new FileWriter(filename)) {
                    wr.write(textAreaGraphFile.getText());
                } catch (Exception exc) {
                    SwingUtils.showErrorMessageBox(exc);
                }
            }
        });

        buttonCreateGraph.addActionListener(e -> {
            try {
                Graph graph = GraphUtils.fromStr(textAreaGraphFile.getText());
                panelGraphPainter.paint(dotToSvg(graph));
            } catch (Exception exc) {
                SwingUtils.showErrorMessageBox(exc);
            }
        });

        buttonSaveGraphSvgToFile.addActionListener(e -> {
            if (panelGraphPainter.svg == null) {
                return;
            }
            if (fileChooserImgSave.showSaveDialog(FrameMain.this) == JFileChooser.APPROVE_OPTION) {
                String filename = fileChooserImgSave.getSelectedFile().getPath();
                if (!filename.toLowerCase().endsWith(".svg")) {
                    filename += ".svg";
                }
                try (FileWriter wr = new FileWriter(filename)) {
                    wr.write(panelGraphPainter.svg);
                } catch (Exception exc) {
                    SwingUtils.showErrorMessageBox(exc);
                }
            }
        });
    }

    /**
     * Преобразование dot-записи в svg-изображение (с помощью Graphviz)
     *
     * @return svg
     */
    private static String dotToSvg(Graph graph) throws IOException {
        MutableGraph g = new Parser().read(GraphUtils.toDot(graph));
        Collection<MutableNode> nodes = g.nodes();

        ColorSampling sampling = new ColorSampling(graph);
        Map<Integer, String> map = sampling.assignColorVertices();

        for (MutableNode node : nodes) {
            Color color = Color.named(map.get(Integer.parseInt(node.name().toString())));
            node.add(color);
        }

        return Graphviz.fromGraph(g).render(Format.SVG).toString();
    }
}
