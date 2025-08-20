private Interface GameOfLife
{
    public void randomInitialize(double aliveProbability);
    public void loadFromString(String data);
    public void loadFromFile(String filename) throws FileNotFoundException;
    public int countLiveNeighbors(int r, int c);
    public void nextGeneration();
    public boolean isAlive(int r, int c);
    public int numRows();
    public int numCols();
    public boolean isStillLife();
}