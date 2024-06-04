import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.FileWriter;

public class ComposeDiary {
    private final User currUser;// 当前用户
    private final UserManagement userManagement;// 用户管理对象
    private final SpotManagement spotManagement;// 景点管理对象
    private final HuffmanCode huffmanCode;

    public ComposeDiary(StudyDiarySystem studyDiarySystem) {
        this.currUser = studyDiarySystem.getCurrUser();
        this.userManagement = studyDiarySystem.getUserManagement();
        this.spotManagement = studyDiarySystem.getSpotManagement();
        this.huffmanCode = new HuffmanCode();
    }


    public void addDiary(String title, String content, int rating, String location) {
        //对日记进行writeIn
        //int diaryId = userManagement.getdiarynum(currUser);
        int diraryId =0;
            //日记格式为 user名字+"_"+diraryId+"compress.txt"


        String diaryName = currUser.getName()+"_"+diraryId+".txt";


        String diaryName_title = currUser.getName()+"_"+diraryId+"title.txt";
        //写入diaryName
        try(BufferedWriter writer = new BufferedWriter(new FileWriter("./diary/"+diaryName))){
            writer.write(content);
        }catch (Exception e){
            e.printStackTrace();
        }
        //写入diaryName_title,写入rating，location
        try(BufferedWriter writer = new BufferedWriter(new FileWriter("./diary_title/"+diaryName_title))) {
            writer.write(title);
            writer.write("\n");
            writer.write(String.valueOf(rating));
            writer.write("\n");
            writer.write(location);
            writer.write("\n");
            writer.write("0");      //浏览量
        }catch (Exception e){
            e.printStackTrace();
        }

        //将日记写入文件

        huffmanCode.writeInFile(diaryName);


        //userManagement.setdiarynum(currUser,diaryId);
    }


}
