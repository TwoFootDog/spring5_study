package spring;

import java.time.format.DateTimeFormatter;

import org.springframework.lang.Nullable;

public class MemberPrinter {
	private DateTimeFormatter dateTimeFormatter;
	
	
	
	
	public MemberPrinter() {
		super();
		dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy�� MM�� dd��");
	}

	public void print(Member member) {
/*		System.out.printf("ȸ�� ���� : ���̵� = %d, �̸��� = %s, �̸� = %s, ����� = %tF\n",
				member.getId(), member.getEmail(), member.getName(), member.getRegisterDateTime());*/
		if (dateTimeFormatter == null) {
			System.out.printf("ȸ�� ���� : ���̵� = %d, �̸��� = %s, �̸� = %s, ����� = %tF\n",
					member.getId(), member.getEmail(), member.getName(), member.getRegisterDateTime());
		} else {
			System.out.printf("ȸ�� ���� : ���̵� = %d, �̸��� = %s, �̸� = %s, ����� = %sF\n",
					member.getId(), member.getEmail(), member.getName(), dateTimeFormatter.format(member.getRegisterDateTime()));
		}
		
	}
	
/*	@Autowired(required = false)
	public void setDateFormatter(DateTimeFormatter dateTimeFormatter) {
		this.dateTimeFormatter = dateTimeFormatter;
	}*/
	public void setDateFormatter(@Nullable DateTimeFormatter dateTimeFormatter) {
		this.dateTimeFormatter = dateTimeFormatter;
	}
}
 