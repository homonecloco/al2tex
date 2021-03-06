// Al2Tex
//
// Alignment Diagrams in LaTeX
//
// Copyright 2012 Richard Leggett
// richard.leggett@tgac.ac.uk
// 
// This is free software, supplied without warranty.

package al2tex;

import java.io.*;
import java.util.*;
import java.awt.image.*;
import java.awt.*;
import javax.imageio.*;

public class DiagramOptions {
    private String diagramType = null;
    private String inputFilename = null;
    private String outputDirectory = null;
    private String outputFilename = "diagram.tex";
    private String listFilename = null;
    private String inputFormat = null;
    private String domainsFilename = null;
    private int maxCoverage = 0;
    private int maxPages = 0;
    private int maxTargets = 0;
    private int tSize = 0;
    private int rowHeight = 10;
    private int rowSpacer = 0;
    private boolean newHeatMapForEachContig = false;
    
    public void parseArgs(String[] args) {
        int i=0;
        
        if (args.length <= 1) {
            System.out.println("Syntax al2tex [options]");
            System.out.println("");
            System.out.println("Options:");
            System.out.println("    -type coverage|coveragemap|alignment|all");
            System.out.println("    -inputfmt psl|coords|pileup|tiling|sam");
            System.out.println("    -in <filename>");
            System.out.println("    -outdir <directory>");
            System.out.println("    -out <leafname>");
            System.out.println("    -maxtargets <int>");
            System.out.println("    -maxcoverage <int>");
            System.out.println("    -tsize <int>");
            System.out.println("");
            System.exit(1);
        }
        
        while (i < (args.length-1)) {
            if (args[i].equalsIgnoreCase("-type")) {
                diagramType = args[i+1].toLowerCase();
                if ((!diagramType.equals("coverage")) &&
                   (!diagramType.equals("coveragemap")) &&
                   (!diagramType.equals("alignment")) &&
                   (!diagramType.equals("all"))) {
                    System.out.println("Error: type must be 'all', 'coverage', 'coveragemap' or 'alignment'.");
                    System.exit(0);
                }
                                
                System.out.println("    Diagram type: " + diagramType);
            } else if (args[i].equalsIgnoreCase("-inputfmt")) {
                inputFormat = args[i+1].toLowerCase();
                if ((!inputFormat.equals("psl")) &&
                    (!inputFormat.equals("coords")) &&
                    (!inputFormat.equals("pileup")) &&
                    (!inputFormat.equals("sam")) &&
                    (!inputFormat.equals("tiling"))) {
                    System.out.println("Error: inputfmt must be 'psl', 'coords', 'pileup', 'sam' or 'tiling'.");
                    System.exit(0);
                }
            } else if (args[i].equalsIgnoreCase("-in")) {
                inputFilename = args[i+1];
                System.out.println("  Input filename: "+inputFilename);
            } else if (args[i].equalsIgnoreCase("-list")) {
                listFilename = args[i+1];
                System.out.println("   List filename: "+listFilename);
            } else if (args[i].equalsIgnoreCase("-domains")) {
                domainsFilename = args[i+1];
                System.out.println("Domains filename: "+domainsFilename);
            } else if (args[i].equalsIgnoreCase("-outdir")) {                
                outputDirectory = args[i+1];
                System.out.println("Output directory: "+outputDirectory);
            } else if (args[i].equalsIgnoreCase("-out")) {                
                outputFilename = args[i+1];
                System.out.println(" Output filename: "+outputFilename);
            } else if (args[i].equalsIgnoreCase("-maxtargets")) {
                maxTargets = Integer.parseInt(args[i+1]);
                System.out.println("     Max targets: " + maxTargets);
            } else if (args[i].equalsIgnoreCase("-maxcoverage")) {
                maxCoverage = Integer.parseInt(args[i+1]);
                System.out.println("    Max coverage: " + maxCoverage);
            } else if (args[i].equalsIgnoreCase("-tsize")) {
                tSize = Integer.parseInt(args[i+1]);
                System.out.println("     Target size: " + tSize);
            } else if (args[i].equalsIgnoreCase("-rowheight")) {
                rowHeight = Integer.parseInt(args[i+1]);
                System.out.println("      Row height: " + rowHeight);
            } else if (args[i].equalsIgnoreCase("-rowspacer")) {
                rowSpacer = Integer.parseInt(args[i+1]);
                System.out.println("      Row spacer: " + rowSpacer);
            } else {
                System.out.println("Unknown paramter: " + args[i]);
                System.exit(0);
            }
            
            i+=2;
        }
        
        if (inputFormat == null) {
            System.out.println("Error: You must specify an input format");
            System.exit(0);
        }

        if ((diagramType.equals("coveragemap")) && (!inputFormat.equals("psl")) && (!inputFormat.equals("sam"))) {
            System.out.println("Error: coverage maps are currently only possible for PSL files");
            System.exit(0);
        }        
        
        if (inputFormat.equals("sam")) {
            if (!diagramType.equals("coveragemap")) {
                System.err.println("Error: SAM files can only produce coverage maps at the moment");
            }
            
            if (tSize == 0) {
                System.out.println("Error: For SAM files, you must specify a -tsize");
            }
        }

        if (inputFormat.equals("pileup")) {
            if (!diagramType.equals("coverage")) {          
                System.out.println("Error: For pileup files, you can only plot coverage diagrams.");
                System.exit(0);
            }
            if (outputFilename == null) {
                System.out.println("Error: you must specify a -out parameter.");
                System.exit(0);
            }            
            if (listFilename == null) {
                System.out.println("Error: you must specify a -list parameter.");
                System.exit(0);
            }
        } else {        
            if (diagramType == null) {
                System.out.println("Error: you must specify a -type parameter.");
                System.exit(0);
            }        

            if (inputFormat == null) {
                System.out.println("Error: you must specify a -inputfmt parameter.");
                System.exit(0);
            }        

            if (inputFilename == null) {
                System.out.println("Error: you must specify a -in parameter.");
                System.exit(0);
            }        

            if (outputDirectory == null) {
                System.out.println("Error: you must specify a -outdir parameter.");
                System.exit(0);
            }               
        }
    }        
    
    public String getInputFilename() {
        return inputFilename;
    }
    
    public String getInputFormat() {
        return inputFormat;
    }
    
    public String getOutputDirectory() {
        return outputDirectory;
    }
    
    public String getOutputFilename() {
        return outputFilename;
    }
    
    public String getListFilename() {
        return listFilename;
    }

    public String getDomainsFilename() {
        return domainsFilename;
    }
    
    public int getMaxCoverage() {
        return maxCoverage;
    }
    
    public int getTargetSize() {
        return tSize;
    }
    
    public String getDiagramType() {
        return diagramType;
    }
    
    public int getMaxTargets() {
        return maxTargets;
    }
    
    public String filterName(String s) {
        String a = s.replace("|", "$|$");
        String b = a.replace("_", "\\_");
        
        return b;
    }
    
    public String getInputFilenameLeaf() {
        File f = new File(inputFilename);
        return f.getName();
    }
    
    public String getOutputFilePath() {
        return outputDirectory+File.separator+outputFilename;
    }
    
    public boolean isNewHeatMapForEachContig() {
        return newHeatMapForEachContig;
    }
    
    public int getRowHeight() {
        return rowHeight;
    }
    
    public int getRowSpacer() {
        return rowSpacer;
    }
}
