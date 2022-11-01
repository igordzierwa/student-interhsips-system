package agh.studentInternshipSupportSystem.Internships;

import agh.studentInternshipSupportSystem.Authentication.JwtConfig;
import agh.studentInternshipSupportSystem.Exceptions.RecordNotFoundException;
import agh.studentInternshipSupportSystem.User.LoggedUserDTO;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.layout.font.FontProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.text.StyleConstants;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.channels.IllegalChannelGroupException;

@Controller
public class InternshipCertificate {

    public InternshipCertificate(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public void generate() {

    }

    @Autowired
    InternshipRepository internshipRepository;

    @Autowired
    JwtConfig jwtConfig;


    private final TemplateEngine templateEngine;


    @RequestMapping(path = "/test_cert")
    public String getCertPage(Model model) throws IOException {

        //model.addAttribute("orderEntry", );
        return "cert";
    }


    @RequestMapping(path = "/certificate")
    public ResponseEntity<?> getPDF(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String header = request.getHeader(jwtConfig.getHeader());
        LoggedUserDTO loggedUserDTO;
        if (header != null) {
            loggedUserDTO = jwtConfig.getResponseSession(header);
        } else {
            throw new RecordNotFoundException("No user defined");
        }

        Internship internship = internshipRepository.findByStudentUsername(loggedUserDTO.getUsername());

        if(!(internship.isUniversitySupervisorAccepted() && internship.isCompanySupervisorAccepted()))
        {
            throw new IllegalArgumentException("Internship not accepted");
        }


        WebContext context = new WebContext(request, response, request.getServletContext());
        context.setVariable("certData", internship);
        context.setVariable("studentData", internship.getStudent());
        context.setVariable("usData", internship.getStudent().getUniversitySupervisor());
        context.setVariable("csData", internship.getCompanySupervisor());
        context.setVariable("companyData", internship.getCompanySupervisor().getCompany());
        context.setVariable("departmentData", internship.getStudent().getUniversitySupervisor().getDepartment());

        context.setVariable("certDataString", internship.toString());
        String orderHtml = templateEngine.process("cert", context);


        ByteArrayOutputStream target = new ByteArrayOutputStream();

        ConverterProperties converterProperties = new ConverterProperties();
        converterProperties.setBaseUri("http://localhost:9090");

        FontProvider fontProvider  = new FontProvider();
        //fontProvider.addFont("/path/to/my-font.ttf");
        fontProvider.addStandardPdfFonts();
        fontProvider.addSystemFonts();
        converterProperties.setFontProvider(fontProvider);

        HtmlConverter.convertToPdf(orderHtml, target, converterProperties);

        byte[] bytes = target.toByteArray();


        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .body(bytes);
    }
}
