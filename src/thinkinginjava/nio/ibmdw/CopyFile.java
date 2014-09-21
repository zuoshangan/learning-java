package thinkinginjava.nio.ibmdw;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class CopyFile {
	static public void main(String args[]) throws Exception {
		if (args.length < 2) {
			System.err.println("Usage: java CopyFile infile outfile");
			System.exit(1);
		}

		String infile = args[0];
		String outfile = args[1];

		// �����л�ȡͨ��
		FileInputStream fin = new FileInputStream(infile);
		FileOutputStream fout = new FileOutputStream(outfile);

		FileChannel fcin = fin.getChannel();
		FileChannel fcout = fout.getChannel();

		// ����������
		ByteBuffer buffer = ByteBuffer.allocate(1024);

		while (true) {
			// ����֮ǰҪ���
			buffer.clear();

			// position�Զ�ǰ��
			int r = fcin.read(buffer);

			if (r == -1) {
				break;
			}

			// position = 0; limit=�������ֽ���
			buffer.flip();

			// �� buffer �ж�
			fcout.write(buffer);
		}
	}
}