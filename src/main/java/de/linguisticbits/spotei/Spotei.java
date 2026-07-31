/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package de.linguisticbits.spotei;

import de.linguisticbits.spotei.annotation.OrthoNormalizer;
import de.linguisticbits.spotei.annotation.TreeTagger;
import de.linguisticbits.spotei.utils.NameSpaceCleanup;
import de.linguisticbits.spotei.utils.XSLTHelper;
import de.linguisticbits.spotei.utils.XSLTHelperFactory;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdom.JDOMException;

/**
 *
 * @author bernd
 */
public class Spotei {

private static void printHelpStatement() {
    System.out.println("""
Spotei - Conversion and processing of ISO-TEI spoken corpora

Usage:
    spotei [options] <input-file> <output-file>

Options:
    -h, --help
        Show this help message.

    -c, --command <command>
        Add a processing command. Multiple commands are executed
        in the order in which they appear.

    -p, --param NAME=VALUE
        Set a parameter. Parameters are available to all commands.
        Multiple parameters may be specified.

Example:
    spotei \\
        --command exmaralda2isotei \\
        --command normalize \\
        --command tokenize \\
        --param TRANSCRIPTION_SYSTEM=HIAT \\
        --param USE_XPOINTER=false \\
        input.exb output.xml

Available commands

  Conversion
    exmaralda2isotei
    exmaralda2isotei_eventtoken
    folker2isotei
    isotei2exmaralda
    isotei2exmaralda_keeptokens
    isotei2eaf
    isotei2folker
    isotei2tcf
    isotei2vtt

  Processing
    addLowLevelAnchors
    attributes2spans
    desegment
    detokenize
    flattenSegHierarchy
    insertSentenceLayer
    interpolate
    normalize
    removeStrayAnchors
    removeTimepointsWithoutAbsolute
    segment
    spans2attributes
    time2tokenSpanReference
    token2timeSpanReference
    tokenize
                       
    orthoNormalize
    treeTag                                              

Common parameters

    LANGUAGE=<iso639-3>
    TRANSCRIPTION_SYSTEM=<system>
    USE_XPOINTER=true|false

For command-specific parameters, consult the documentation.
""");
}    


    public Spotei() throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        xsltHelper = XSLTHelperFactory.newXSLTHelper();
    }

    public static void main(String[] args) {
        try {
            if (args.length==0){
                String[] commands = {
                    "exmaralda2isotei",
                    "normalize",
                    "tokenize",
                    "orthoNormalize",
                    "treeTag"
                };
                String[][] parameters = {
                    {"LANGUAGE", "en"},
                    {"TRANSCRIPTION_SYSTEM", "HIAT"},
                    {"USE_XPOINTER", "FALSE"},
                    {"USE_XPOINTER", "FALSE"},
                    {"CONFIGURATION_FILE", "C:\\spotei\\spotei\\src\\main\\java\\de\\linguisticbits\\spotei\\annotation\\TreeTaggerSampleConfiguration.xml"}
                };

                File inFile = new File("C:\\spotei\\spotei\\src\\main\\java\\data\\beckhams.exb");
                File outFile = new File("C:\\spotei\\spotei\\src\\main\\java\\data\\beckhams_xxx.xml");

                Spotei spotei = new Spotei();

                spotei.processCommands(commands, parameters, inFile, outFile);
                
                NameSpaceCleanup.CleanupNameSpaces(outFile);
                
                System.exit(0);                
            }
            
            if (args.length==1 && (args[0].equals("--help") || (args[0].equals("-h")))){
                printHelpStatement();
            }
            
            if (args.length < 3){
                System.out.println("You need to specify at least one command, an input file and an output file. ");
                System.exit(1);
            }
            
            File inFile = new File(args[args.length - 2]);
            if (!(inFile.exists() && inFile.canRead())){
                System.out.println("File " + args[args.length-2] + " cannot be read. ");
                System.exit(1);
            }
            
            File outFile = new File(args[args.length - 1]);
            if (!outFile.canWrite()){
                System.out.println("File " + args[args.length-1] + " cannot be written to. ");
                System.exit(1);
            }
            
            List<String> commands = new ArrayList<>();
            List<String> parameters = new ArrayList<>();
            for (int i=1; i<args.length-2; i+=2){
                String token = args[i];
                String previous = args[i-1];
                if (previous.equals("--command") || previous.equals("c")) {
                    commands.add(token);
                } else if (previous.equals("--param") || previous.equals("p")){
                    parameters.add(token);
                } else {
                    System.out.println("Cannot interpret " + previous + " " + token);
                    System.exit(1);
                }
            }
            
            String[] commandArray = commands.toArray(String[]::new);
            String[][] parameterArray = new String[parameters.size()][2];
            int index = 0;
            for (String paramString : parameters){
                int i = paramString.indexOf("=");
                if (i<0){
                    System.out.println("Wrong format for parameter: " + paramString);
                    System.exit(1);
                }
                String parameterName = paramString.substring(0, i-1);
                String parameterValue = paramString.substring(i+1);
                parameterArray[index][0] = parameterName;
                parameterArray[index][1] = parameterValue;
                index++;
            }
            
            
            Spotei spotei = new Spotei();

            spotei.processCommands(commandArray, parameterArray, inFile, outFile);

            NameSpaceCleanup.CleanupNameSpaces(outFile);
            
            System.exit(0);                
            
            
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | IOException ex) {
            Logger.getLogger(Spotei.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
    public void processCommands (String[] commands, String[][] parameters, File inFile, File outFile) throws IOException{
        File currentInFile = inFile;
        File tempOutFile = File.createTempFile("Spotei", ".xml");
        for (String command : commands){
            processCommand(command, parameters, currentInFile, tempOutFile);
            currentInFile = tempOutFile;
        }
        Files.copy(tempOutFile.toPath(), outFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        tempOutFile.delete();
        
    }
    
   public void processCommand(String command, String[][] parameters,
                           File inFile, File outFile) throws IOException {

    switch (command.toLowerCase()) {

        //******************************************************
        //********             CONVERSION              *********
        //******************************************************

        case "exmaralda2isotei":
            exmaralda2isotei(inFile, outFile, parameters);
            break;

        case "exmaralda2isotei_eventtoken":
            exmaralda2isotei_eventToken(inFile, outFile, parameters);
            break;

        case "folker2isotei":
            folker2isotei(inFile, outFile, parameters);
            break;

        case "isotei2exmaralda":
            isotei2exmaralda(inFile, outFile, parameters);
            break;

        case "isotei2exmaralda_keeptokens":
            isotei2exmaralda_keepTokens(inFile, outFile, parameters);
            break;

        case "isotei2eaf":
            isotei2eaf(inFile, outFile);
            break;

        case "isotei2folker":
            isotei2folker(inFile, outFile, parameters);
            break;

        case "isotei2tcf":
            isotei2tcf(inFile, outFile);
            break;

        case "isotei2vtt":
            isotei2vtt(inFile, outFile);
            break;

        //******************************************************
        //********             PROCESSING              *********
        //******************************************************

        case "addlowlevelanchors":
            addLowLevelAnchors(inFile, outFile);
            break;

        case "attributes2spans":
            attributes2spans(inFile, outFile);
            break;

        case "desegment":
            desegment(inFile, outFile, parameters);
            break;

        case "detokenize":
            detokenize(inFile, outFile, parameters);
            break;

        case "flattenseghierarchy":
            flattenSegHierarchy(inFile, outFile);
            break;

        case "insertsentencelayer":
            insertSentenceLayer(inFile, outFile, parameters);
            break;

        case "interpolate":
            interpolate(inFile, outFile);
            break;

        case "normalize":
            normalize(inFile, outFile);
            break;

        case "removestrayanchors":
            removeStrayAnchors(inFile, outFile);
            break;

        case "removetimepointswithoutabsolute":
            removeTimepointsWithoutAbsolute(inFile, outFile);
            break;

        case "segment":
            segment(inFile, outFile, parameters);
            break;

        case "spans2attributes":
            spans2attributes(inFile, outFile);
            break;

        case "time2tokenspanreference":
            time2tokenSpanReference(inFile, outFile, parameters);
            break;

        case "token2timespanreference":
            token2timeSpanReference(inFile, outFile, parameters);
            break;

        case "tokenize":
            tokenize(inFile, outFile, parameters);
            break;
            
        /// NON-XSLT-Processing
            
        case "orthonormalize" :
            orthoNormalize(inFile, outFile);
            break;

        case "treetag" :
            treeTag(inFile, outFile, parameters);
            break;

        default:
            System.out.println("Unknown command: " + command);
            throw new IllegalArgumentException("Unknown command: " + command);
    }
}
    

    
    
    XSLTHelper xsltHelper;
    
    private void printParameters(String[][] parameters){
        for (String[] parameter : parameters){
            System.out.print(parameter[0] + ":" + parameter[1] + " ");
        }        
    }
    
    //******************************************************
    //********             CONVERSION              *********
    //******************************************************
    
    public void exmaralda2isotei(File inFile, File outFile, String[][] parameters) throws IOException {
        System.out.print("[Spotei] exmaralda2isotei " + inFile.getAbsolutePath() + " " + outFile.getAbsolutePath() + " ");
        printParameters(parameters);
        System.out.println("");
        xsltHelper.transformXSLT(SpoteiConstants.EXMARALDA2ISOTEI_XSLT, inFile, outFile, parameters);        
    }
    
    public void exmaralda2isotei_eventToken(File inFile, File outFile, String[][] parameters) throws IOException {
        System.out.print("[Spotei] exmaralda2isotei_eventToken " + inFile.getAbsolutePath() + " " + outFile.getAbsolutePath() + " ");
        printParameters(parameters);
        System.out.println("");
        xsltHelper.transformXSLT(SpoteiConstants.EXMARALDA2ISOTEI_EVENTTOKEN_XSLT, inFile, outFile, parameters);        
    }

    public void folker2isotei(File inFile, File outFile, String[][] parameters) throws IOException {
        System.out.print("[Spotei] folker2isotei " + inFile.getAbsolutePath() + " " + outFile.getAbsolutePath() + " ");
        printParameters(parameters);
        System.out.println("");
        xsltHelper.transformXSLT(SpoteiConstants.FOLKER2ISOTEI_XSLT, inFile, outFile, parameters);        
    }

    public void isotei2exmaralda(File inFile, File outFile, String[][] parameters) throws IOException {
        System.out.print("[Spotei] isotei2exmaralda " + inFile.getAbsolutePath() + " " + outFile.getAbsolutePath() + " ");
        printParameters(parameters);
        System.out.println("");
        xsltHelper.transformXSLT(SpoteiConstants.ISOTEI2EXMARALDA_XSLT, inFile, outFile, parameters);        
    }

    public void isotei2exmaralda_keepTokens(File inFile, File outFile, String[][] parameters) throws IOException {
        System.out.print("[Spotei] isotei2exmaralda_keepTokens " + inFile.getAbsolutePath() + " " + outFile.getAbsolutePath() + " ");
        printParameters(parameters);
        System.out.println("");
        xsltHelper.transformXSLT(SpoteiConstants.ISOTEI2EXMARALDA_KEEPTOKENS_XSLT, inFile, outFile, parameters);        
    }

    public void isotei2eaf(File inFile, File outFile) throws IOException {
        System.out.println("[Spotei] isotei2eaf " + inFile.getAbsolutePath() + " " + outFile.getAbsolutePath() + " ");
        xsltHelper.transformXSLT(SpoteiConstants.ISOTEI2EAF_XSLT, inFile, outFile);        
    }

    public void isotei2folker(File inFile, File outFile, String[][] parameters) throws IOException {
        System.out.print("[Spotei] isotei2folker " + inFile.getAbsolutePath() + " " + outFile.getAbsolutePath() + " ");
        printParameters(parameters);
        System.out.println("");
        xsltHelper.transformXSLT(SpoteiConstants.ISOTEI2FOLKER_XSLT, inFile, outFile, parameters);        
    }

    public void isotei2tcf(File inFile, File outFile) throws IOException {
        System.out.println("[Spotei] isotei2tcf " + inFile.getAbsolutePath() + " " + outFile.getAbsolutePath() + " ");
        xsltHelper.transformXSLT(SpoteiConstants.ISOTEI2TCF_XSLT, inFile, outFile);        
    }

    public void isotei2vtt(File inFile, File outFile) throws IOException {
        System.out.println("[Spotei] isotei2vtt " + inFile.getAbsolutePath() + " " + outFile.getAbsolutePath() + " ");
        xsltHelper.transformXSLT(SpoteiConstants.ISOTEI2VTT_XSLT, inFile, outFile);        
    }

    //******************************************************
    //********             PROCESSING              *********
    //******************************************************

    public void addLowLevelAnchors(File inFile, File outFile) throws IOException{
        System.out.println("[Spotei] addLowLevelAnchors " + inFile.getAbsolutePath() + " " + outFile.getAbsolutePath() + " ");
        xsltHelper.transformXSLT(SpoteiConstants.ADDLOWLEVELANCHORS_XSLT, inFile, outFile);
    }

    public void attributes2spans(File inFile, File outFile) throws IOException{
        System.out.println("[Spotei] attributes2spans " + inFile.getAbsolutePath() + " " + outFile.getAbsolutePath() + " ");
        xsltHelper.transformXSLT(SpoteiConstants.ATTRIBUTES2SPANS_XSLT, inFile, outFile);
    }

    public void desegment(File inFile, File outFile, String[][] parameters) throws IOException{
        System.out.print("[Spotei] desegment " + inFile.getAbsolutePath() + " " + outFile.getAbsolutePath() + " ");
        printParameters(parameters);
        System.out.println("");
        xsltHelper.transformXSLT(SpoteiConstants.DESEGMENT_XSLT, inFile, outFile, parameters);
    }

    public void detokenize(File inFile, File outFile, String[][] parameters) throws IOException{
        System.out.print("[Spotei] detokenize " + inFile.getAbsolutePath() + " " + outFile.getAbsolutePath() + " ");
        printParameters(parameters);
        System.out.println("");
        xsltHelper.transformXSLT(SpoteiConstants.DETOKENIZE_XSLT, inFile, outFile, parameters);
    }

    public void flattenSegHierarchy(File inFile, File outFile) throws IOException{
        System.out.println("[Spotei] flattenSegHierarchy " + inFile.getAbsolutePath() + " " + outFile.getAbsolutePath() + " ");
        xsltHelper.transformXSLT(SpoteiConstants.FLATTENSEGHIAERARCHY_XSLT, inFile, outFile);
    }

    public void insertSentenceLayer(File inFile, File outFile, String[][] parameters) throws IOException{
        System.out.print("[Spotei] insertSentenceLayer " + inFile.getAbsolutePath() + " " + outFile.getAbsolutePath() + " ");
        printParameters(parameters);
        System.out.println("");
        xsltHelper.transformXSLT(SpoteiConstants.INSERTSENTENCELAYER_XSLT, inFile, outFile, parameters);
    }

    public void interpolate(File inFile, File outFile) throws IOException{
        System.out.println("[Spotei] interpolate " + inFile.getAbsolutePath() + " " + outFile.getAbsolutePath() + " ");
        xsltHelper.transformXSLT(SpoteiConstants.INTERPOLATE_XSLT, inFile, outFile);
    }

    public void normalize(File inFile, File outFile) throws IOException{
        System.out.println("[Spotei] normalize " + inFile.getAbsolutePath() + " " + outFile.getAbsolutePath() + " ");
        xsltHelper.transformXSLT(SpoteiConstants.NORMALIZE_XSLT, inFile, outFile);
    }
    
    public void removeStrayAnchors(File inFile, File outFile) throws IOException{
        System.out.println("[Spotei] removeStrayAnchors " + inFile.getAbsolutePath() + " " + outFile.getAbsolutePath() + " ");
        xsltHelper.transformXSLT(SpoteiConstants.REMOVESTRAYANCHORS_XSLT, inFile, outFile);
    }

    public void removeTimepointsWithoutAbsolute(File inFile, File outFile) throws IOException{
        System.out.println("[Spotei] removeTimepointsWithoutAbsolute " + inFile.getAbsolutePath() + " " + outFile.getAbsolutePath() + " ");
        xsltHelper.transformXSLT(SpoteiConstants.REMOVETIMEPOINTSWITHOUTABSOLUTE_XSLT, inFile, outFile);
    }

    public void segment(File inFile, File outFile, String[][] parameters) throws IOException{
        System.out.print("[Spotei] segment " + inFile.getAbsolutePath() + " " + outFile.getAbsolutePath() + " ");
        printParameters(parameters);
        System.out.println("");
        xsltHelper.transformXSLT(SpoteiConstants.SEGMENT_XSLT, inFile, outFile, parameters);
    }

    public void spans2attributes(File inFile, File outFile) throws IOException{
        System.out.println("[Spotei] spans2attributes " + inFile.getAbsolutePath() + " " + outFile.getAbsolutePath() + " ");
        xsltHelper.transformXSLT(SpoteiConstants.SPANS2ATTRIBUTES_XSLT, inFile, outFile);
    }

    public void time2tokenSpanReference(File inFile, File outFile, String[][] parameters) throws IOException{
        System.out.print("[Spotei] time2tokenSpanReference " + inFile.getAbsolutePath() + " " + outFile.getAbsolutePath() + " ");
        printParameters(parameters);
        System.out.println("");
        xsltHelper.transformXSLT(SpoteiConstants.TIME2TOKENSPANREFERENCES_XSLT, inFile, outFile, parameters);
    }

    public void token2timeSpanReference(File inFile, File outFile, String[][] parameters) throws IOException{
        System.out.print("[Spotei] token2timeSpanReference " + inFile.getAbsolutePath() + " " + outFile.getAbsolutePath() + " ");
        printParameters(parameters);
        System.out.println("");
        xsltHelper.transformXSLT(SpoteiConstants.TOKEN2TIMESPANREFERENCES_XSLT, inFile, outFile, parameters);
    }

    public void tokenize(File inFile, File outFile, String[][] parameters) throws IOException{
        System.out.print("[Spotei] tokenize " + inFile.getAbsolutePath() + " " + outFile.getAbsolutePath() + " ");
        printParameters(parameters);
        System.out.println("");
        xsltHelper.transformXSLT(SpoteiConstants.TOKENIZE_XSLT, inFile, outFile, parameters);
    }
    
    
    public void orthoNormalize(File inFile, File outFile) throws IOException{
        try {
            System.out.println("[Spotei] orthoNormalize " + inFile.getAbsolutePath() + " " + outFile.getAbsolutePath() + " ");
            OrthoNormalizer.orthoNormalize(inFile, outFile);
        } catch (JDOMException ex) {
            Logger.getLogger(Spotei.class.getName()).log(Level.SEVERE, null, ex);
            throw new IOException(ex);
        }
    }
    
    public void treeTag(File inFile, File outFile, String[][] parameters) throws IOException{
        try {
            System.out.print("[Spotei] treeTag " + inFile.getAbsolutePath() + " " + outFile.getAbsolutePath() + " ");
            printParameters(parameters);
            System.out.println("");
            String pathToConfig = null;
            for (String[] paramPair : parameters){
                if (paramPair[0].equalsIgnoreCase("configuration_file")){
                    pathToConfig = paramPair[1];
                    break;
                }
            }
            if (pathToConfig==null){
                System.out.println("[Spotei] No configuration path found in " + pathToConfig);
                throw new IOException("[Spotei] No configuration path found in " + pathToConfig);
            }
            File configFile = new File(pathToConfig);
            if (!(configFile.exists() && configFile.canRead())){
                System.out.println("[Spotei] Cannot read configuration at " + pathToConfig);
                throw new IOException("[Spotei] Cannot read configuration at " + pathToConfig);
                
            }
            TreeTagger.treeTag(inFile, outFile, configFile);
        } catch (JDOMException ex) {
            Logger.getLogger(Spotei.class.getName()).log(Level.SEVERE, null, ex);
            throw new IOException(ex);
        }
    }

    //******************************************************
    //********             OUTPUT                  *********
    //******************************************************

}
