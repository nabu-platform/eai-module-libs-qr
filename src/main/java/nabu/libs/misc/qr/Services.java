package nabu.libs.misc.qr;

import io.nayuki.qrcodegen.QrCode;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

@WebService
public class Services {
	@WebResult(name = "svg")
	public InputStream toSvg(@WebParam(name = "string") String text) {
		if (text == null) {
			return null;
		}
		QrCode qr = QrCode.encodeText(text, QrCode.Ecc.MEDIUM);
		return new ByteArrayInputStream(qr.toSvgString(4).getBytes(Charset.forName("UTF-8")));
	}
	@WebResult(name = "image")
	public InputStream toImage(@WebParam(name = "string") String text, @WebParam(name = "contentType") String contentType) throws IOException {
		if (text == null) {
			return null;
		}
		if (contentType == null) {
			contentType = "image/png";
		}
		QrCode qr = QrCode.encodeText(text, QrCode.Ecc.MEDIUM);
		BufferedImage image = qr.toImage(4, 10);
		Iterator<ImageWriter> writers = ImageIO.getImageWritersByMIMEType(contentType);
		if (!writers.hasNext()) {
			throw new IllegalArgumentException("No handler for the content type: " + contentType);
		}
		ImageWriter writer = writers.next();
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		ImageOutputStream imageOutput = ImageIO.createImageOutputStream(output);
		writer.setOutput(imageOutput);
		writer.write(image);
		imageOutput.flush();
		return new ByteArrayInputStream(output.toByteArray());
	}
}
