package app.util.feature;

import app.util.feature.FeatureFunction; 
import app.util.feature.RegexFeature; 
import app.util.feature.Section; 
import app.util.feature.WordStorage; 

import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.operator.Operator;

import java.util.Properties;

public class ContractFunction { 
       private FeatureFunction featureFunction; 
       private WordStorage wordStorage; 

        public ContractFunction(FeatureFunction featureFunction, WordStorage wordStorage) { 
               this.featureFunction = featureFunction; 
               this.wordStorage = wordStorage; 
        } 

        Operator gteq = new Operator(">=", 2, true, Operator.PRECEDENCE_ADDITION - 1) {

              @Override
              public double apply(double[] values) {
                  if (values[0] >= values[1]) {
                      return 1d;
                  } else {
                      return 0d;
                  }
              }
          };

          Operator lteq = new Operator("<=", 2, true, Operator.PRECEDENCE_ADDITION - 1) {

              @Override
              public double apply(double[] values) {
                  if (values[0] <= values[1]) {
                      return 1d;
                  } else {
                      return 0d;
                  }
              }
          };

          Operator gt = new Operator(">", 2, true, Operator.PRECEDENCE_ADDITION - 1) {

              @Override
              public double apply(double[] values) {
                  if (values[0] > values[1]) {
                      return 1d;
                  } else {
                      return 0d;
                  }
              }
          };

          Operator lt = new Operator("<", 2, true, Operator.PRECEDENCE_ADDITION - 1) {

              @Override
              public double apply(double[] values) {
                  if (values[0] < values[1]) {
                      return 1d;
                  } else {
                      return 0d;
                  }
              }
          };

        public boolean preconditionExists(RegexFeature regexFeature) { 
               Boolean exists=false; 
               String pre=null; 
               if (regexFeature!=null) { 
                      pre = regexFeature.getPrecondition(); 
                      if ((pre!=null) && (pre.length()>0)) { 
                            pre = pre.trim();
                            if (pre.contains("=")) {
                                 exists = true;
                             } 
                      } 
               } 
           return exists;             
        } 

        public boolean postconditionExists(RegexFeature regexFeature) { 
               Boolean exists=false; 
               String post=null; 
               if (regexFeature!=null) { 
                      post = regexFeature.getPostcondition(); 
                      if ((post!=null) && (post.length()>0)) { 
                            exists = true; 
                   } 
               } 
              return exists;             
        } 

        public boolean doPrecondition(RegexFeature regexFeature, Section section) { 
               Boolean passed=false; 
               String precondition=null; 
               ContractFunction tempContractFunction=null; 
               RegexFeature preFeature=null; 
               Integer count=0; 
               if (regexFeature!=null) { 
                      precondition = regexFeature.getPrecondition(); 
                      preFeature = new RegexFeature("test", "test", "test", precondition, "text", "", ""); 
                      tempContractFunction = new ContractFunction(featureFunction, wordStorage); 
                      Matcher matcher = new Matcher(preFeature, featureFunction, wordStorage, tempContractFunction); 
                       try { 
                          count = matcher.matchcount(section); 
                       } catch (ParseRegexException regexException) { 
                              regexException.printStackTrace();
                              count=0; 
                       } 
                       if (count>0) { 
                             passed=true; 
                       } 
               } 
        return passed;             
        } 

        public boolean doPostcondition(RegexFeature regexFeature, Properties properties) { 
               Boolean passed=false; 
               Double result=0.0;
               Expression expression=null;
               String postcondition=null; 
               String value=""; 
               Integer amount=0; 
               if (regexFeature!=null) { 
                      postcondition = regexFeature.getPostcondition(); 
                      postcondition = populateProperties(postcondition, properties);
                      if ((postcondition!=null) && (postcondition.length()>0)) { 
                             try {
                                   expression = new ExpressionBuilder(postcondition)
                                             .operator(gteq)
                                             .operator(lteq)
                                             .operator(gt)
                                             .operator(lt)
                                             .build();
                                   result = expression.evaluate();
                             } catch (Exception exception) {
                                  result=-1*1.0;
                             }
                           
                            if (result>0) {
                                   passed=true;
                            }
                      } 
               } 
          return passed;              
        } 

        private String populateProperties(String postcondition, Properties properties) {
            String value="";
            String key="";
            String replacement=postcondition;
            for (Object objectKey:properties.keySet()) {
                   key = (String)objectKey;
                   value = properties.getProperty(key);
                   if (replacement.contains(key)) {
                          replacement=replacement.replaceAll("\\"+key,value);
                   }
            }
           return replacement;
        }

        private Integer getNumber(String value) { 
               Integer number=0; 
               try { 
                         number = Integer.parseInt(value); 
               } catch (NumberFormatException exception) { 
                         number=-1; 
               } 
              return number;  
        } 

} 