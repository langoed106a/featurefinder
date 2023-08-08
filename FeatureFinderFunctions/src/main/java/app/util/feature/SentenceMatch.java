package app.util.feature;

import java.util.List;
import java.util.ArrayList;

public class SentenceMatch {
   private List<List<PartMatch>> matches;
   private String firstsentence;
   private String secondsentence;

    public SentenceMatch(String firstId, String secondId) {
        this.firstsentence = firstId;
        this.secondsentence = secondId;
        matches = new ArrayList<>();
    }

    public void addMatch(List<PartMatch> theSameList) {
        List<PartMatch> partMatchList=new ArrayList<>();
        PartMatch partMatch=null;
        for (PartMatch match:theSameList) {
            partMatch = new PartMatch(match.getFirst(), match.getSecond(), match.getMatch());
            partMatchList.add(partMatch);
        }
        matches.add(partMatchList);
    }

    public void removeRepeatedGroups() {
        Boolean check=false;
        Integer matchesIndex=0, index=0;
        List<Integer> indexes=new ArrayList<>();
        List<PartMatch> partMatch=null;
        List<List<PartMatch>> newMatches = new ArrayList<>();
        matchesIndex=0;
        while (matchesIndex<matches.size()) {
            partMatch = matches.get(matchesIndex);
            check = isRepeatedGroup(partMatch, matchesIndex);
            if (check) {
                indexes.add(matchesIndex);
            }
            matchesIndex++;
        }
        if (indexes.size()>0) {
            matchesIndex=0;
            while (matchesIndex<matches.size()) {
                if (!indexes.contains(matchesIndex)) {
                     partMatch = matches.get(matchesIndex);
                     newMatches.add(partMatch);
                }
                matchesIndex++;
            }
          this.matches = newMatches;
        }
    }

    public Boolean isRepeatedGroup(List<PartMatch> partMatch, Integer currentIndex) {
         Boolean remove = false, finish=false, found=false;
         Integer size=0, first=0, last=0, firstIndex=0, lastIndex=0;
         Integer matchIndex=0, partIndex=0;
         List<PartMatch> partMatches=null;
         if ((partMatch!=null) && (partMatch.size()>0)) {
             size = partMatch.size();
             first = (partMatch.get(0)).getFirst();
             last = (partMatch.get(size-1)).getFirst();
         }
         matchIndex=0;
         while ((matchIndex<matches.size()) && (!finish)) {
            partMatches = matches.get(matchIndex);
            size = partMatches.size();
            firstIndex = (partMatches.get(0)).getFirst();
            lastIndex = (partMatches.get(size-1)).getFirst();
            if ((first>=firstIndex) && (last<=lastIndex) && (matchIndex!=currentIndex)) {
                remove=true;
                finish=true;
            }
            matchIndex++;
        }
       return remove;
    } 


    public String getFirst() {
        return firstsentence;
    }

    public String getSecond() {
        return secondsentence;
    }

    public List<List<PartMatch>> getMatches() {
        return matches;
    }
}