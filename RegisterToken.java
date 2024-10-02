
public class RegisterToken extends Token {
	// Token to signify which Register [0 to 31]
	private Bit[] binaryData = new Bit[5]; // Binary Representation of data	
	private int data = -1;
	
	RegisterToken (Token.TokenType type, int data) {
        super(type);
        this.data = data;
        int temp = data;
        for (int i = 4; i >= 0; i--) {
            binaryData[4-i] = new Bit();
            if (temp >= Math.pow(2, i)) {
                binaryData[4-i].set();
                temp -= Math.pow(2, i);
            }
        }
	}
	
	public int getData() {
		return this.data;
	}
	
	public Bit[] getBinaryData() {
		return this.binaryData;
	}
	
	@Override
	public String toString() {
		return "R" + this.getData();
	}
}
