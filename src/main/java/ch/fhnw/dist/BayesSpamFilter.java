package ch.fhnw.dist;

import ch.fhnw.dist.evaluate.Word;
import ch.fhnw.dist.filter.SpamFilter;
import ch.fhnw.dist.reader.ZipReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class BayesSpamFilter {

    private static final Logger logger = LogManager.getLogger(BayesSpamFilter.class);
    private static Map<String, Word> words = new HashMap<>();
    private static final boolean DEBUG = false;

    public static void main(String[] args) {
        logger.info("starting Bayes-Spam-Filter");


        /**
         * New SpamFilter Instance
         */
        SpamFilter spamFilter = new SpamFilter();

        /**
         * Load Files for test from classpath ressources
         * TODO Maybe load files from Java Arguments on start
         */

        /**
         * Learn all Ham Mails
         */
        File hamFiles = new File(BayesSpamFilter.class.getClassLoader().getResource("Programmieraufgabe1" + File.separator + "ham-anlern.zip").getFile());
        ZipReader hr = new ZipReader(hamFiles.toString());
        String[] hamMails = hr.doRead();

        logger.info("start learning HAM content");
        for (String mail : hamMails) {
            spamFilter.learn(mail, SpamFilter.SpamOrHam.HAM);
        }
        logger.info("finished learning HAM content");


        /**
         * Learn all Spam Mails
         */
        File spamFiles = new File(BayesSpamFilter.class.getClassLoader().getResource("Programmieraufgabe1" + File.separator + "spam-anlern.zip").getFile());
        ZipReader sr = new ZipReader(spamFiles.toString());
        String[] spamMails = sr.doRead();

        logger.info("start learning SPAM content");
        for (String mail : spamMails) {
            spamFilter.learn(mail, SpamFilter.SpamOrHam.SPAM);
        }
        logger.info("finished learning SPAM content");


        /**
         * Check Mails for spam
         */
        spamFiles = new File(BayesSpamFilter.class.getClassLoader().getResource("Programmieraufgabe1" + File.separator + "spam-test.zip").getFile());
        sr = new ZipReader(spamFiles.toString());
        spamMails = sr.doRead();

        logger.info("Test Spam Mails");
        int testedSpamMails = spamMails.length;
        int wrongTestedSpamMails = 0;
        for (String mail : spamMails) {
            if (spamFilter.checkMail(mail) != SpamFilter.SpamOrHam.SPAM) {
                wrongTestedSpamMails++;
            }
        }
        logger.info("finished testing SPAM content");
        logger.error("NaNCounter: " + spamFilter.getNanCounter());

        spamFilter.resetNanCounter();

        /**
         * Check Mails for ham
         */
        hamFiles = new File(BayesSpamFilter.class.getClassLoader().getResource("Programmieraufgabe1" + File.separator + "ham-test.zip").getFile());
        hr = new ZipReader(hamFiles.toString());
        hamMails = hr.doRead();

        logger.info("Test Ham Mails");
        int testedHamMails = hamMails.length;
        int wrongTestedHamMails = 0;
        for (String mail : hamMails) {
            if (spamFilter.checkMail(mail) != SpamFilter.SpamOrHam.HAM) {
                wrongTestedHamMails++;
            }
        }
        logger.info("finished testing HAM content");

        logger.error("NaNCounter: " + spamFilter.getNanCounter());

        spamFilter.resetNanCounter();

        logger.info(" Checked Spam Mails: " + testedSpamMails + " => Wrong classified: " + wrongTestedSpamMails + " => Percentage of right classification: " + (int)((1.0 - (double) wrongTestedSpamMails / (double) testedSpamMails)*100) + "%");
        logger.info(" Checked Ham Mails: " + testedHamMails + " => Wrong classified: " + wrongTestedHamMails + " => Percentage of right classification: " + (int)((1.0 - (double) wrongTestedHamMails/ (double) testedHamMails)*100) + "%");


        if (DEBUG) {
            /**
             * Output words and Spam/Ham count to console | Just for debugging purposes
             */
            spamFilter.printWordlist();

            /**
             * Write all words and Spam/Ham count to model.csv | Just for debugging purposes
             */
            spamFilter.printWordsToFile();
        }

    }
}
