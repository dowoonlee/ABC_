import sys
from PyQt5.QtWidgets import QApplication, QWidget, QLabel, QGridLayout, QTextEdit, QPushButton, QProgressBar, QVBoxLayout
from PyQt5.QtCore import QBasicTimer

import requests
from bs4 import BeautifulSoup
import numpy as np





def gaussratio(rank):

    movement = 0.1
    TotalUserNumber = 3981594
    score = TotalUserNumber-rank
    sig = 3e2
    AA = 1873

    xx = np.arange(-500, 2501, 0.5)
    yy = (AA * np.exp(-((xx - 1000) / 2 / sig) ** 2))

    dif = 100
    r = 1200
    step = 0
    while (dif>10)and(step<1e4):
        step+=1
        score0 = np.sum(yy[np.where((xx<r))])
        if score0 > score:
            r -= movement
        else:
            r += movement
        dif = abs(score-score0)
    return r

class Searching():
    def __init__(self, Name):

        Tdist = np.array([0.37, 0.93, 1.5, 2.9, 2, 4.1, 5.9, 6.9, 11, 8.2, 8.8, 6.1, 11, 6.4, 4.8, 2.8, 4.9, 1.8, 1.2, 1.2, 1.4, 0.53, 0.29, 0.17, 0.036, 0.032, 0.13])[::-1]



        TotalUserNumber = 3981594

        url='https://www.op.gg/summoner/userName=' + Name
        hdr = {'Accept-Language': 'ko_KR,en;q=0.8', 'User-Agent': ('Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.70 Mobile Safari/537.36')}
        req = requests.get(url, headers=hdr)
        html = req.text
        soup = BeautifulSoup(html, 'html.parser')
        self.soup = soup

        SummonerName = ""
        Ranking = ""
        Wins = []
        Losses = []
        Ratio = []

        positions = []

        #소환사 이름 크롤링
        for i in soup.select('div[class=SummonerName]'):
            SummonerName = i.text
        #랭킹 크롤링
        for i in soup.select('span[class=ranking]'):
            Ranking = i.text
        # 승리 패배 판수 크롤링
        for i in soup.select('span[class=Wins]'):
            Wins.append(i.text)
        for i in soup.select('span[class=Losses]'):
            Losses.append(i.text)

        for i in soup.select('div[class=Name]'):
            positions.append(i.text)
        positions = soup.find("div", {"class": "Name"})
        positions = soup.select('div.Name')


        #승률 크롤링
        for i in soup.select('span[class=Ratio]'):
            Ratio.append(i.text)

        self.Summoner = SummonerName
        self.Ratio = len(Wins)/(len(Wins)+len(Losses))


        if Ranking == '':
            self.Rank = int(2 * TotalUserNumber / 3)

        else:
            a, b= len(Ranking)//4, len(Ranking)%4
            Rank = 0
            for i in range(a):
                x = 1 + i*4
                Rank += int(Ranking[b + x : b + x + 3])*10**(3*(a-i-1))
            Rank += int(Ranking[0:b])*10**(3*a)
            self.Rank = Rank



        RGS = soup.select('h2.Text')[:]
        RecentGames = len(RGS)

        self.KDA = 0

        Container = {}
        Container['Name'] = soup.find("div", {"class": "SummonerName"}).text

        i = soup.select('div.KDARatio')[0]
        Container['KDARatio'] = i.text.replace('\n', '').replace('\t', '')
        i = soup.select('div.Stats')[0]
        Container['Stats'] = i.text.replace('\n', '').replace('\t', '')


        MeanD, MeanA = 0,0

        for lll in range(RecentGames):

            champ = soup.find("div", {"class": "ChampionImage"})
            champ = champ.find("a").attrs['href']
            Container['Champion'] = champ.replace("/champion/", '').replace("/statistics", '').capitalize()

            i = soup.select('h2.Text')[lll]
            Container['MatchInfo'] = i.text.replace('\n', '').replace('\t', '').strip('\n \t')

            i = soup.select('div.Right')[lll]
            Container['Time'] = i.text.replace('\n', '').replace('\t', ' ')

            d = soup.select('div.GameItemList span.Death')[lll]
            a = soup.select('div.GameItemList span.Assist')[lll]

            MeanD += int(d.text)
            MeanA += int(a.text)


        self.MeanD = MeanD/(RecentGames - 1)
        self.MeanA = MeanA/(RecentGames - 1)


        elo_pred = gaussratio(self.Rank)


        KDA = Container['KDARatio'].split(':')
        self.KDA = float(KDA[0])
        Stats = Container['Stats']
        self.KDAper = int(Stats[-4:-1])


        RecentRate = 0.1*((self.Ratio-0.5)*100/25)*100
        RecentKDA = (self.KDA - 3)*10
        RecentKDAper = (self.KDAper/100-0.5) * 9
        self.elo = elo_pred + RecentRate + RecentKDA + RecentKDAper

    def elos(self):
        return self.elo


def find_user(Summoner, Scores):
    Summoner = Summoner[np.argsort(Scores)]
    score = Scores[np.argsort(Scores)]

    team = np.zeros(10)
    team[0], team[9] = 0, 0
    At = score[0] + score[9]

    team[1], team[8] = 1, 1
    Bt = score[1] + score[8]

    tmp1 = score[2] + score[7]
    tmp2 = score[3] + score[6]

    if (At > Bt):
        if (tmp1 > tmp2):
            team[2], team[7] = 1, 1
            team[3], team[6] = 0, 0
        else:
            team[2], team[7] = 0, 0
            team[3], team[6] = 1, 1
    else:
        if (tmp1 > tmp2):
            team[2], team[7] = 0, 0
            team[3], team[6] = 1, 1
        else:
            team[2], team[7] = 1, 1
            team[3], team[6] = 0, 0

    At = np.sum(score[np.where(team == 0)]) - score[4] - score[5]
    Bt = np.sum(score[np.where(team == 1)])

    if At > Bt:
        team[4], team[5] = 1, 0
    else:
        team[4], team[5] = 0, 1

    Ascore = score[np.where(team == 0)]
    Bscore = score[np.where(team == 1)]
    Ateam = Summoner[np.where(team == 0)]
    Bteam = Summoner[np.where(team == 1)]

    s1 = 'Ateam(%d) : ' % np.sum(Ascore), Ateam[0], '/', Ateam[1], '/', Ateam[2], '/', Ateam[3], '/', Ateam[4]
    s2 = 'Bteam(%d) : ' % np.sum(Bscore), Bteam[0], '/', Bteam[1], '/', Bteam[2], '/', Bteam[3], '/', Bteam[4]

    return Ascore, Bscore, Ateam, Bteam


class MainApp(QWidget):

    def __init__(self):
        super().__init__()

        self.layout = QGridLayout()
        self.initUI()
        self.Ateam, self.Bteam = [], []
        self.Ascore, self.Bscore = 0.0, 0.0
        self.Scores = np.zeros(10)
        self.Summoner = np.array([])
        self.s1 = ''

    def initUI(self):
        self.btn = QPushButton("Get Balanced", self)
        self.btn.setGeometry(15, 320, 130, 30)
        self.btn.clicked.connect(self.doAction)

        self.lbl1 = QLabel("Summoners :")
        self.te = QTextEdit()
        self.te.setAcceptRichText(False)
        self.lbl2 = QLabel('The number of Summoners is 0/10')
        self.lbl3 = QLabel('')
        self.lbl4 = QLabel('')



        vbox = QVBoxLayout()
        vbox.addWidget(self.lbl1)
        vbox.addWidget(self.te)
        vbox.addWidget(self.lbl2)
        vbox.addWidget(self.lbl3)
        vbox.addWidget(self.lbl4)
        vbox.addStretch()

        self.setLayout(vbox)

        self.layout.addWidget(self.btn, 2, 3)
        self.setWindowTitle('이누벨 V1.02')

        self.te.textChanged.connect(self.text_changed)



        self.pbar = QProgressBar(self)
        self.pbar.setGeometry(150, 310, 820, 50)

        self.timer = QBasicTimer()
        self.step = 0

        self.setGeometry(300, 300, 300, 200)


        self.move(500, 300)
        self.resize(1000, 350)
        self.show()

    def timerEvent(self, e):
        if self.step >= 100:
            self.timer.stop()
            self.Ascore, self.Bscore, self.Ateam, self.Bteam = find_user(self.Summoner, self.Scores)
            #for i in range(5):
            #    print(self.Ateam[i], '%d'%self.Ascore[i])
            #    print(self.Bteam[i], '%d'%self.Bscore[i])
            self.s1 = 'Ateam(%d) : %s/ %s/ %s/ %s/ %s' % (np.sum(self.Ascore), self.Ateam[0], self.Ateam[1], self.Ateam[2], self.Ateam[3], self.Ateam[4])
            self.s2 = 'Bteam(%d) : %s/ %s/ %s/ %s/ %s' % (np.sum(self.Bscore), self.Bteam[0], self.Bteam[1], self.Bteam[2], self.Bteam[3], self.Bteam[4])
            self.lbl3.setText(self.s1)
            self.lbl4.setText(self.s2)
            return


        Name = self.text.split('\n')[int(self.step/10)]
        a = Searching(Name)
        self.Scores[int(self.step/10)] = a.elos()
        self.Summoner = np.concatenate((self.Summoner, [Name]), axis=0)
        self.step = self.step + 10
        self.pbar.setValue(self.step)

    def doAction(self):
        if self.timer.isActive():
            self.timer.stop()
            self.btn.setText("Get Balanced")
        else:
            self.timer.start(100, self)
            self.btn.setText('Stop')

    def text_changed(self):
        self.text = self.te.toPlainText()
        self.lbl2.setText('The number of Summoners is ' + str(len(self.text.split('\n'))) + '/10')






if __name__ == '__main__':
    app = QApplication(sys.argv)
    ex = MainApp()
    sys.exit(app.exec_())
