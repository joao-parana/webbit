package org.webbitserver.handler;

import org.apache.log4j.Logger;

public class StaticFile implements TemplateEngine {
	private static Logger logger = Logger.getLogger(StaticFile.class);

	@Override
	public byte[] process(byte[] template, String templatePath,
			Object templateContext) {
		logger.info("templatePath = " + templatePath);

		return template;
	}
}
