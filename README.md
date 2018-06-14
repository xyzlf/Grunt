# Grunt
Android注解器实践

Grunt：war3里面，兽族步兵的称号。希望view的初始化，能像兽族步兵一样，打起仗来，简单好用。

# Gradle

    dependencies {
        implementation 'com.xyzlf.apt:apt-api:0.0.1' //依赖Api
        annotationProcessor 'com.xyzlf.apt:apt-processor:0.0.1' //依赖注解器
    }

# 使用方式

    public class MainActivity extends AppCompatActivity {

        @BindView(R.id.text)
        TextView textView;
        @BindView(R.id.button)
        Button button;
        @BindView(R.id.image)
        ImageView imageView;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            ViewInjector.injectView(this); //在布局文件之后
        }
    }


# 关于我

有任何使用问题，可以给我发邮件：

Author：张利峰

E-mail：519578280@qq.com

# License

    Copyright(c)2018 xyzlf Open Source Project

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.