import java.io.FileReader;

import javax.xml.bind.JAXBContext;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;

import com.polopoly.xml.app.InputTemplate;
import com.polopoly.xml.app.OutputTemplate;
import com.polopoly.xml.app.TemplateDefinition;


public class ParseXML {
    public static void main(String[] args) throws Exception
    {
        JAXBContext context = JAXBContext.newInstance("com.polopoly.xml.app");
        XMLInputFactory xmlif = XMLInputFactory.newInstance();
        xmlif.setXMLResolver(new BogusResolver());
        XMLEventReader reader = xmlif.createXMLEventReader(new FileReader("/home/jwiklund/.usr/src/polopoly/extensions/tablet/src/main/content/templates/templates.xml"));
        TemplateDefinition td = (TemplateDefinition) context.createUnmarshaller().unmarshal(reader);
        for (Object d : td.getInputTemplateOrOutputTemplate()) {
            if (d instanceof InputTemplate) {
                print((InputTemplate) d);
            } else {
                print((OutputTemplate) d);
            }
        }
    }

    private static void print(OutputTemplate d)
    {
        System.out.println("OutputTemplate " + d.getName());
    }

    private static void print(InputTemplate d)
    {
        System.out.println("InputTemplate " + d.getName());
    }
}
