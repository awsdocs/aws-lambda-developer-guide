package example;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Object;
import software.amazon.awssdk.services.s3.S3Client;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification.S3EventNotificationRecord;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Handler value: example.Handler
public class Handler implements RequestHandler<S3Event, String> {
  Gson gson = new GsonBuilder().setPrettyPrinting().create();
  private static final Logger logger = LoggerFactory.getLogger(Handler.class);
  private static final float MAX_WIDTH = 100;
  private static final float MAX_HEIGHT = 100;
  private final String JPG_TYPE = (String) "jpg";
  private final String JPG_MIME = (String) "image/jpeg";
  private final String PNG_TYPE = (String) "png";
  private final String PNG_MIME = (String) "image/png";
  @Override
  public String handleRequest(S3Event s3event, Context context) {
    try {
      logger.info("EVENT: " + gson.toJson(s3event));
      S3EventNotificationRecord record = s3event.getRecords().get(0);
      
      String srcBucket = record.getS3().getBucket().getName();

      // Object key may have spaces or unicode non-ASCII characters.
      String srcKey = record.getS3().getObject().getUrlDecodedKey();

      String dstBucket = srcBucket;
      String dstKey = "resized-" + srcKey;

      // Infer the image type.
      Matcher matcher = Pattern.compile(".*\\.([^\\.]*)").matcher(srcKey);
      if (!matcher.matches()) {
          logger.info("Unable to infer image type for key " + srcKey);
          return "";
      }
      String imageType = matcher.group(1);
      if (!(JPG_TYPE.equals(imageType)) && !(PNG_TYPE.equals(imageType))) {
          logger.info("Skipping non-image " + srcKey);
          return "";
      }

      // Download the image from S3 into a stream
      S3Client s3Client = S3Client.builder().build();
      GetObjectRequest getObjectRequest = GetObjectRequest.builder()
        .bucket(srcBucket)
        .key(srcKey)
        .build();
      InputStream s3Object = s3Client.getObject(getObjectRequest);

      // Read the source image
      BufferedImage srcImage = ImageIO.read(s3Object);
      int srcHeight = srcImage.getHeight();
      int srcWidth = srcImage.getWidth();
      // Infer scaling factor to avoid stretching image unnaturally
      float scalingFactor = Math.min(
        MAX_WIDTH / srcWidth, MAX_HEIGHT / srcHeight);
      int width = (int) (scalingFactor * srcWidth);
      int height = (int) (scalingFactor * srcHeight);

      BufferedImage resizedImage = new BufferedImage(width, height,
              BufferedImage.TYPE_INT_RGB);
      Graphics2D g = resizedImage.createGraphics();
      // Fill with white before applying semi-transparent (alpha) images
      g.setPaint(Color.white);
      g.fillRect(0, 0, width, height);
      // Simple bilinear resize
      g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
              RenderingHints.VALUE_INTERPOLATION_BILINEAR);
      g.drawImage(srcImage, 0, 0, width, height, null);
      g.dispose();

      // Re-encode image to target format
      ByteArrayOutputStream os = new ByteArrayOutputStream();
      ImageIO.write(resizedImage, imageType, os);
      // InputStream is = new ByteArrayInputStream(os.toByteArray());

      Map<String, String> metadata = new HashMap<>();
      metadata.put("Content-Length", Integer.toString(os.size()));
      if (JPG_TYPE.equals(imageType)) {
        metadata.put("Content-Type", JPG_MIME);
      } else if (PNG_TYPE.equals(imageType)) {
        metadata.put("Content-Type", PNG_MIME);
      }
      PutObjectRequest putObjectRequest = PutObjectRequest.builder()
        .bucket(dstBucket)
        .key(dstKey)
        .metadata(metadata)
        .build();

      // Uploading to S3 destination bucket
      logger.info("Writing to: " + dstBucket + "/" + dstKey);
      try {
        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(os.toByteArray()));
      }
      catch(AwsServiceException e)
      {
        logger.error(e.awsErrorDetails().errorMessage());
        System.exit(1);
      }
      logger.info("Successfully resized " + srcBucket + "/"
              + srcKey + " and uploaded to " + dstBucket + "/" + dstKey);
      return "Ok";
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}