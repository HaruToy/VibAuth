# VibAuth

### 데이터 수집 방법
1. 워치 안드로이드 스튜디오에 연결
    
    콘솔 창에 'adb connect [아이피][포트]' 입력
    
2. 로그 내용을 텍스트 파일로 변환
    
    콘솔 창에 'adb -s [아이피][포트] logcat ACCEL:V *:S>accel.txt'
    
3. 텍스트 파일 엑셀 파일로 변환
    아래 파일 파이썬 파일로 생성해 실행 (파일 위치 변경해서 사용)
    
       ```python
       import pandas as pd
       text_file_path = 'C:/Users/twitt/accel.txt'
       new_text_content = 'time,x,y,z\n'
       with open(text_file_path,'r') as f:
       lines = f.readlines()
       for i in enumerate(lines):
       new_text_content+=i[1][43:]
       with open('C:/Users/twitt/accel1.txt','w') as f:
       f.write(new_text_content)
       df = pd.DataFrame(pd.read_csv('C:/Users/twitt/accel1.txt',sep=','))
       print(df)

       df.to_excel('accel.xlsx',index=False)
        ```
