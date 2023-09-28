package app.util.feature;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.gui.beans.DataSource;

import libsvm.svm_parameter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WekaModelGenerator { 
   private static final Logger logger = LoggerFactory.getLogger(WekaModelGenerator.class);

   public WekaModelGenerator() {

   }

   public Instances buildDataset(String data) {
       ArffLoader loader = null;
       DataSource source = null;
       Instances dataSet = null;
       InputStream inputStream = null;
       try {
             loader = new ArffLoader();
             inputStream = new ByteArrayInputStream(data.getBytes());
             loader.setSource(inputStream);
             dataSet = loader.getDataSet();
             dataSet.setClassIndex(dataSet.numAttributes()-1);
        } catch (Exception exception) {
            logger.error("Failed to create weka dataset:"+exception.getMessage());
        }
       return dataSet;
   }

   public String getConfusionMatrix(String filepath, List<String> data, List<String> namesList) {
       Classifier mode = null;
       Evaluation eval = null;
       Instances instanceDataset = null;
       List<String> arffData = null;
       Random random = new Random(1); //use seed = 1
       String matrix="", arffStr="";
       int folds = 0;
       try {
            model = this.loadModelFromFile(filepath);
            arffData = this.convertToArff(data, namesList, true);
            instanceDataset = this.buildDataset(arffStr);
            folds = 5;
            eval = new Evaluation(instanceDataset);
            eval.crossValidateModel(model, instanceDataset, folds, random);
            matrix = eval.toMatrixString();
       } catch (Exception exception) {
           logger.error("Failed to get confusion matrix:"+exception.getMessage());
       }
     return matrix;
   }

   public List<String> convertToArff(List<String> data, List<String> classNames, Boolean hasHeader) {
       List<String> arffData = new ArrayList<>();
       Integer index = 0;
       String header="", columnName="", classRow="", className="", dataRow="", dataValue="", dataArff="", name="";
       String[] columns = null;
       if ((hasHeader) && (data!=null) && (data.size()>0)) {
           arffData.add("@relation featuredata");
           header = data.get(0);
           columns = header.split(",");
           if (columns.length>0) {
               for (int i=0; i<columns.length; i++) {
                   columnName = columns[i];
                   header = "@attribute "+columnName+" REAL";
                   arffData.add(header);
               }
           }
           if (classNames.size()>0) {
               classRow = "";
               index = 0;
               while (index<classNames.size()) {
                   name = classNames.get(index);
                   classRow = classRow + name + ",";
                   index++;
               }
               classRow = classRow.substring(0, classRow.length()-1);
               classRow = "@attribute class {" + classRow + "}";
               arffData.add(classRow);
           }
           /* add the data rows */
           arffData.add("@data");
           index = 1;
           while (index<data.size()) {
               dataRow = data.get(index);
               columns = dataRow.split(",");
               dataArff = "";
               for (int i=0; i<columns.length; i++) {
                   dataValue = columns[i];
                   dataArff = dataArff + dataValue + ",";
               }
               dataArff = dataArff.substring(0, dataArff.length()-1);
               arffData.add(dataArff);
               index++;
           }
       }
      return arffData;
   }

   public List<String> getClassNames(List<String> data) {
       List<String> classNames = new ArrayList<>();
       Boolean found = false;
       Integer index = 1;
       String dataRow="", className="", item="";
       String[] columns = null;
       while ((index<data.size()) && (data.size()>=index)) {
           dataRow = data.get(index);
           columns = dataRow.split(",");
           className = columns[columns.length-1];
           className = className.toLowerCase();
           found = false;
           for (int p=0; p<classNames.size();p++) {
               item = classNames.get(p);
               if (item.equalsIgnoreCase(className)) {
                   found = true;
               }
           }
           if (!found) {
               classNames.add(className);
           }
           index++;
       }
       return classNames;
   }

   public String convertArffToString(List<String> arffData) {
       StringBuilder stringBuilder = new StringBuilder();
       for (String data:arffData) {
           stringBuilder.append(data);
           stringBuilder.append("\n");
       }
       return stringBuilder.toString();
   }

   public Classifier buildClassifier(Instances dataSet) {
       LibSVM svm = new LibSVM();
       try {
           svm_parameter pre = new svm_parameter();
           pre.kernel_type = svm_parameter.RBF;
           pre.gamma = 0.01;
           pre.C = 100;
           svm.buildClassifier(dataSet);
       } catch (Exception exception) {
           logger.error("Failed to build classifier:"+exception.getMessage());
       }
       return svm;
   }

   public String classify(String modelname, List<String> results, List<String> namesList, Boolean hasHeader) {
       Classifier model=null;
       List<String> arffData=null;
       String arffStr="", dataValues="", headerValue="", resultValue="";
       Instances instanceDataset=null;
       Integer resultIndex=0;
       Double doubleResult = null;
       try {
            arffData = this.convertToArff(results, namesList, hasHeader);
            arffStr = this.convertArffToString(arffData);
            instanceDataset = this.buildDataset(arffStr);
            model=this.loadModelFromFile(modelname);
            if ((model!=null) && (instanceDataset!=null)) {
                doubleResult = model.classifiyInstance(instanceDataset.firstInstance());
                resultIndex = doubleResult.intValue();
                if (resultIndex>=0) {
                    resultValue = namesList.get(resultIndex);
                }
            }
       } catch (Exception exception) {
           logger.error("Failed to classify data:"+exception.getMessage());
       }
       return resultValue;
    }

    public Boolean saveModelToFile(Classifier model, String filepath) {
        Boolean saved=true;
        try {
            SerializationHelper.write(filepath, model);
        } catch (Exception exception) {
            logger.error("Failed to save weka model at:"+filepath+" with error:"+exception.getMessage());
            saved = false;
        }
        return saved;
    }


    public Classifier loadModelFromFile(String filepath) {
        Classifier model = null;
        try {
            model = (LibSVM) SerializationHelper.read(filepath);
        } catch (Exception exception) {
            logger.error("Failed to load model from:"+filepath+" error:"+exception.getMessage());
        }
      return model;
    }

    public Boolean createWekaModel(List<String> results, Boolean hasHeader, List<String> classnames, String pathToSave) {
        Boolean success = false;
        Classifier wekaClassifier = null;
        Instances arffDataset = null;
        List<String> arffData = null;
        String arffStr = "";
        arffData = this.convertToArff(results, classnames, hasHeader);
        arffStr = this.convertArffToString(arffData);
        arffDataset = this.buildDataset(arffStr);
        wekaClassifier = this.buildClassifier(arffDataset);
        success = this.saveModelToFile(wekaClassifier, pathToSave);
        return success;
    }
}
