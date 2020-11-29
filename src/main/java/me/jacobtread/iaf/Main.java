package me.jacobtread.iaf;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    public static void main(String[] args) {
        Logger logger = IAF.LOGGER;
        System.out.printf(" _______               __                                        _______         __         \n" +
                "|_     _|.-----.-----.|  |_.---.-.-----.----.---.-.--------.    |   _   |.--.--.|  |_.-----.\n" +
                " _|   |_ |     |__ --||   _|  _  |  _  |   _|  _  |        |    |       ||  |  ||   _|  _  |\n" +
                "|_______||__|__|_____||____|___._|___  |__| |___._|__|__|__|    |___|___||_____||____|_____|\n" +
                "                                 |_____|                                                    \n" +
                " _______         __ __                                                                      \n" +
                "|    ___|.-----.|  |  |.-----.--.--.--.     Version: %s\n" +
                "|    ___||  _  ||  |  ||  _  |  |  |  |     Use '--help' for arguments and descriptions\n" +
                "|___|    |_____||__|__||_____|________|           \n\n", IAFConstants.VERSION);
        OptionParser optionParser = new OptionParser();
        OptionSpec<?> helpOption = optionParser.acceptsAll(Arrays.asList("h", "help"), "Displays the help message").forHelp();

        OptionSpec<String> usernameOption = optionParser.acceptsAll(Arrays.asList("u", "username", "user"), "The username of the account to target")
                .withRequiredArg()
                .required()
                .ofType(String.class);
        OptionSpec<String> passwordOption = optionParser.acceptsAll(Arrays.asList("p", "password"), "The username of the account to target")
                .withRequiredArg()
                .required()
                .ofType(String.class);
        OptionSpec<?> headOption = optionParser.acceptsAll(Arrays.asList("h", "head"), "Run with browser window head..? why would you want that..");
        OptionSpec<File> logOption = optionParser.acceptsAll(Arrays.asList("l", "log"), "Specify a log file for logs")
                .withRequiredArg()
                .ofType(File.class);
        OptionSpec<?> quietOption = optionParser.acceptsAll(Arrays.asList("q", "quiet"), "Disable console output");
        OptionSet optionSet = optionParser.parse(args);
        if (optionSet.has(quietOption)) {
            IAF.DISABLE_LOGGING = true;
        }
        if (optionSet.has(helpOption)) {
            try {
                optionParser.printHelpOn(System.out);
            } catch (IOException ignored) {
            }
            return;
        }
        if (optionSet.has(logOption)) {
            File logPath = logOption.value(optionSet);
            try {
                logger.addHandler(new FileHandler(logPath.getAbsolutePath()));
            } catch (IOException e) {
                logger.log(Level.WARNING, "Unable to add file logger continuing anyway", e);
            }
        }
        String username = usernameOption.value(optionSet);
        String password = passwordOption.value(optionSet);
        boolean head = optionSet.has(headOption);
        IAF iaf = new IAF(username, password, !head);
        iaf.run();
    }

}
