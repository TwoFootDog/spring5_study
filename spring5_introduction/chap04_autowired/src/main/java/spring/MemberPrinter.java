package spring;

import java.time.format.DateTimeFormatter;

import org.springframework.lang.Nullable;

public class MemberPrinter {
	private DateTimeFormatter dateTimeFormatter;
	
	
	
	
	public MemberPrinter() {
		super();
		dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");
	}

	public void print(Member member) {
/*		System.out.printf("회원 정보 : 아이디 = %d, 이메일 = %s, 이름 = %s, 등록일 = %tF\n",
				member.getId(), member.getEmail(), member.getName(), member.getRegisterDateTime());*/
		if (dateTimeFormatter == null) {
			System.out.printf("회원 정보 : 아이디 = %d, 이메일 = %s, 이름 = %s, 등록일 = %tF\n",
					member.getId(), member.getEmail(), member.getName(), member.getRegisterDateTime());
		} else {
			System.out.printf("회원 정보 : 아이디 = %d, 이메일 = %s, 이름 = %s, 등록일 = %sF\n",
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
 