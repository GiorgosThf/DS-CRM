package gr.digital.systems.crm.utils;

import gr.digital.systems.crm.exception.CrmException;
import java.io.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileUtils {

	private FileUtils() {}

	private static final Logger LOG = LoggerFactory.getLogger(FileUtils.class);

	/**
	 * Checks whether the directory exists and if not it tries to create it.
	 *
	 * @param filePath the path of the file
	 */
	public static void createDirectory(final String filePath) {
		final var file = new File(filePath);

		if (!file.exists()) {
			try {
				var dir = file.mkdirs();
				LOG.info("Directory [{}] created: {} ", filePath, dir);
			} catch (final Exception e) {
				throw new CrmException("Cannot create file");
			}
		}
	}
}
