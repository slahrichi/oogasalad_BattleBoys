/**
 * this will be the main Parser class. It will be responsible
 * for parsing the files that contain the game setups
 */
public class Parser{


  /**
   * This is the Parser constructor
   */
  public Parser(){

  }

  /**
   * this is the main method responsible for parsing. It might have to call determineFileType()
   * to parse according to the file provided.
   */
  public void parseFile(){
    determineFileType();

  }

  /**
   * @return Record containing the parsed Data. The controller will be able to call this method
   */
  public Record extractData(){

  }

  /**
   * this method allows the user to export the current game setup to an external file
   * depending on complexity, we might allow the user to pick the file type they want to export in
   */
  public void exportGame(String fileType){

  }

  /**
   * this method allows us to load a default game should there be issues with the passed file setup
   * e.g. empty file, wrong parameters,...
   */
  public void loadDefault(){

  }


}